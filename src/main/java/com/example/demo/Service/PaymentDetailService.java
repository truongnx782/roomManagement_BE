package com.example.demo.Service;

import com.example.demo.DTO.ServiceDTO;
import com.example.demo.Entity.Contract;
import com.example.demo.Entity.Payment;
import com.example.demo.Entity.PaymentDetail;
import com.example.demo.Repo.ContractRepository;
import com.example.demo.Repo.PaymentDetailRepository;
import com.example.demo.Repo.PaymentRepository;
import com.example.demo.Util.Utils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
public class PaymentDetailService {
    private  final ServiceService serviceService;
    private final PaymentDetailRepository paymentDetailRepository;
    private final PaymentRepository paymentRepository;


    public PaymentDetailService(ServiceService serviceService, PaymentDetailRepository paymentDetailRepository,  PaymentRepository paymentRepository) {
        this.serviceService = serviceService;
        this.paymentDetailRepository = paymentDetailRepository;
        this.paymentRepository = paymentRepository;
    }

    public Object getByPaymentId(Long id, Long cid) {
        Optional<Contract> optionalContract = paymentRepository.findContractByPaymentIdAndCompanyId(id,cid);

        List<Map<String,Object> >ids =  paymentDetailRepository.findByPaymentIdAndCpmpanyId(id,cid);
        Map<String,Object> objectMap = new HashMap<>();
        objectMap.put("ids",ids);
        objectMap.put("paymentId",id);
        BigDecimal sum = BigDecimal.ZERO;
        for (Map<String,Object> map: ids) {
            BigDecimal value = (BigDecimal) map.get("value");
            if(value!= null){
                sum=sum.add((BigDecimal) map.get("value"));
            }
        }
        objectMap.put("rentPrice",optionalContract.get().getRentPrice());
        objectMap.put("sumService",sum);
        objectMap.put("sum",optionalContract.get().getRentPrice().add(sum));

        return objectMap;
    }

    public Object createList(Map<String, Object> payload, Long cid) {
        Long paymentId = Long.valueOf(((Number) payload.get("paymentId")).longValue());
        List<Map<String,Object>> ids = (List<Map<String, Object>>) payload.get("ids");
        List<PaymentDetail> paymentDetails = new ArrayList<>();
        if (paymentId==null) {
            throw new IllegalArgumentException("Payment not found");
        }
        List<PaymentDetail> paymentDetailList = paymentDetailRepository.findAllByPaymentIdAndCompanyId(paymentId,cid);

        if (!paymentDetailList.isEmpty()) {
            for (Map<String, Object> objectMap : ids) {
                paymentDetailRepository.deleteAllByPaymentIdAndCompanyId(paymentId,cid);
                if((Integer) objectMap.get("value")!=0){
                    PaymentDetail paymentDetail = new PaymentDetail();
                    Payment payment = new Payment();
                    payment.setId(paymentId);
                    paymentDetail.setPayment(payment);
                    com.example.demo.Entity.Service service = new com.example.demo.Entity.Service();
                    service.setId(Long.valueOf(((Number) objectMap.get("id")).longValue()));
                    BigDecimal value = new BigDecimal((Long.valueOf(((Number) objectMap.get("value")).longValue())));
                    paymentDetail.setService(service);
                    paymentDetail.setAmountToPay(value);
                    paymentDetail.setStatus(Utils.ACTIVE);
                    paymentDetail.setCompanyId(cid);
                    paymentDetails.add(paymentDetail);
                }
            }
            paymentDetails = paymentDetailRepository.saveAll(paymentDetails);
            return paymentDetails;
        }

        List<ServiceDTO> serviceDTOS = serviceService.getAll(cid);
        for (Map<String,Object> objectMap: ids) {
            if(objectMap.get("value")!= null){
                PaymentDetail paymentDetail = new PaymentDetail();
                Payment payment = new Payment();
                payment.setId(paymentId);
                paymentDetail.setPayment(payment);
                com.example.demo.Entity.Service service = new com.example.demo.Entity.Service();
                service.setId(Long.valueOf(((Number) objectMap.get("id")).longValue()));
                BigDecimal value = new BigDecimal((Long.valueOf(((Number) objectMap.get("value")).longValue())));
                paymentDetail.setService(service);
                BigDecimal heSo = serviceDTOS.stream()
                        .filter(x -> x.getId().equals(service.getId()))
                        .map(ServiceDTO::getServicePrice)
                        .findFirst()
                        .orElse(BigDecimal.ZERO);
                paymentDetail.setAmountToPay(heSo.multiply(value));
                paymentDetail.setStatus(Utils.ACTIVE);
                paymentDetail.setCompanyId(cid);
                paymentDetails.add(paymentDetail);
            }
        }
        paymentDetails= paymentDetailRepository.saveAll(paymentDetails);
        return paymentDetails;
    }
}
