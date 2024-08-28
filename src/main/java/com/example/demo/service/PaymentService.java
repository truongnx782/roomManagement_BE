package com.example.demo.service;

import com.example.demo.DTO.PaymentDTO;
import com.example.demo.entity.Contract;
import com.example.demo.entity.ContractDetail;
import com.example.demo.entity.Payment;
import com.example.demo.entity.PaymentDetail;
import com.example.demo.repository.ContractDetailRepository;
import com.example.demo.repository.ContractRepository;
import com.example.demo.repository.PaymentDetailRepository;
import com.example.demo.repository.PaymentRepository;
import com.example.demo.util.Utils;
import org.antlr.v4.runtime.misc.LogManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PaymentService {
    private static PaymentRepository paymentRepository;
    private static  ContractRepository contractRepository;
    private static ContractDetailRepository contractDetailRepository;
    private static PaymentDetailRepository paymentDetailRepository;


    public PaymentService(PaymentRepository paymentRepository, ContractRepository contractRepository, ContractDetailRepository contractDetailRepository,PaymentDetailRepository paymentDetailRepository) {
        this.paymentRepository = paymentRepository;
        this.contractRepository = contractRepository;
        this.contractDetailRepository = contractDetailRepository;
        this.paymentDetailRepository = paymentDetailRepository;

    }

    public Page<PaymentDTO> search(Map<String, Object> payload, Long cid) {
        PaymentService.autoCreatePayment(cid);

        int page = (int) payload.getOrDefault("page", 0);
        int size = (int) payload.getOrDefault("size", 5);
        String search = (String) payload.getOrDefault("search", "");
        Integer paymentStatus = (Integer) payload.getOrDefault("paymentStatus",null);
        Long roomId = payload.get("roomId") != null ? Long.valueOf(payload.get("roomId").toString()) : null;
        Pageable pageable = PageRequest.of(page, size);
        Page<Payment> data = paymentRepository.search(search, paymentStatus,roomId,cid, pageable);
        PaymentService.autoCreatePayment(cid);
        return data.map(Payment::toDTO);
    }

    public PaymentDTO updatePaymentStatus(Map<String, Object> payload, Long cid) {
        Integer paymentStatus = (Integer) payload.get("checked");
        Long paymentId = Long.valueOf(((Number) payload.get("id")).longValue());
        Optional<Payment> optionalPayment = paymentRepository.findByIdAndCompanyId(paymentId,cid);
        if (!optionalPayment.isPresent()) {
            throw new IllegalArgumentException("contract not found");
        }
        Payment payment = optionalPayment.get();
        payment.setPaymentStatus(paymentStatus);
        payment = paymentRepository.save(payment);
        return Payment.toDTO(payment);
    }

//    @Scheduled(fixedDelay = 6000)
    public static void autoCreatePayment(Long cid){
        List<Payment> paymentList = paymentRepository.findPaymentsWithMaxDatePerContractByCpmpanyId(cid);
        System.out.println(paymentList);
        List<Payment> paymentSave= new ArrayList<>();
        LocalDate localDate = LocalDate.now();
        for (Payment payment: paymentList) {
            if (payment.getPaymentDate().equals(localDate)) {
                Payment p = new Payment();
                p.setId(null);
                p.setContract(payment.getContract());
                p.setAmount(payment.getAmount());
                LocalDate startDate = payment.getPaymentDate();
                LocalDate nextMonthDate = startDate.plusMonths(1);
                p.setPaymentCode("TT-"+ nextMonthDate);
                p.setPaymentDate(nextMonthDate);
                p.setPaymentStatus(Utils.UNPAID);
                p.setStatus(Utils.ACTIVE);
                p.setCompanyId(cid);
                paymentSave.add(p);
                System.out.println("Payment date is before the current date minus 3 days.");
            }
        }
        paymentRepository.saveAll(paymentSave);

        // chuyển trạng thái hợp đồng hết hạn
        LocalDate today = LocalDate.now();
        List<Contract> contracts = contractRepository.updateStatusByCompanyId(cid,today);

        if (!contracts.isEmpty()) {
            // Thu thập tất cả các contract IDs
            List<Long> contractIds = contracts.stream().map(x->x.getId()).collect(Collectors.toList());

            // Tìm tất cả các ContractDetail liên quan
            List<ContractDetail> contractDetails = contractDetailRepository.findByContractIdInAndCompanyId(contractIds, cid);

            // Tìm tất cả các Payments liên quan
            List<Payment> payments = paymentRepository.findAllByContractIdInAndCompanyId(contractIds, cid);

            // Thu thập tất cả các payment IDs
            List<Long> paymentIds = payments.stream().map(x->x.getId()).collect(Collectors.toList());

            // Tìm tất cả các PaymentDetail liên quan
            List<PaymentDetail> paymentDetails = paymentDetailRepository.findAllByPaymentIdsAndCompanyId(paymentIds, cid);

            // Cập nhật trạng thái của tất cả các contracts
            contracts.forEach(contract -> contract.setStatus(Utils.EXPIRED));

            // Cập nhật trạng thái của tất cả các ContractDetails
            contractDetails.forEach(detail -> detail.setStatus(Utils.EXPIRED));

            // Cập nhật trạng thái của tất cả các Payments
            payments.forEach(payment -> payment.setStatus(Utils.EXPIRED));

            // Cập nhật trạng thái của tất cả các PaymentDetails
            paymentDetails.forEach(detail -> detail.setStatus(Utils.EXPIRED));

            // Lưu tất cả các thay đổi vào cơ sở dữ liệu
            contractRepository.saveAll(contracts);
            contractDetailRepository.saveAll(contractDetails);
            paymentRepository.saveAll(payments);
            paymentDetailRepository.saveAll(paymentDetails);
        }

    }
}
