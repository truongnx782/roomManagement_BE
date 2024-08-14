package com.example.demo.Service;

import com.example.demo.DTO.PaymentDTO;
import com.example.demo.Entity.Contract;
import com.example.demo.Entity.Payment;
import com.example.demo.Repo.PaymentDetailRepository;
import com.example.demo.Repo.PaymentRepository;
import com.example.demo.Util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
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

@Service
public class PaymentService {
    private static PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public Page<PaymentDTO> search(Map<String, Object> payload, Long cid) {
        int page = (int) payload.getOrDefault("page", 0);
        int size = (int) payload.getOrDefault("size", 5);
        String search = (String) payload.getOrDefault("search", "");
        Integer paymentStatus = (Integer) payload.getOrDefault("paymentStatus",null);
        Pageable pageable = PageRequest.of(page, size);
        Page<Payment> data = paymentRepository.search(search, paymentStatus,cid, pageable);

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

    @Scheduled(fixedDelay = 300000)
    public static void autoCreatePayment(Long cid){
        List<Payment> paymentList = paymentRepository.findPaymentsWithMaxDatePerContractByCpmpanyId(cid);
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
                p.setPaymentCode("TT - "+ startDate);
                p.setPaymentDate(nextMonthDate);
                p.setPaymentStatus(Utils.UNPAID);
                p.setStatus(Utils.ACTIVE);
                p.setCompanyId(cid);
                paymentSave.add(p);
                System.out.println("Payment date is before the current date minus 3 days.");
            }
        }
        paymentRepository.saveAll(paymentSave);
    }
}
