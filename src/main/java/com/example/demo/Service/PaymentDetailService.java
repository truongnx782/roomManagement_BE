package com.example.demo.Service;

import com.example.demo.DTO.ServiceDTO;
import com.example.demo.Entity.Payment;
import com.example.demo.Entity.PaymentDetail;
import com.example.demo.Repo.PaymentDetailRepository;
import com.example.demo.Util.Utils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PaymentDetailService {
    private  final ServiceService serviceService;
    private final PaymentDetailRepository paymentDetailRepository;

    public PaymentDetailService(ServiceService serviceService, PaymentDetailRepository paymentDetailRepository) {
        this.serviceService = serviceService;
        this.paymentDetailRepository = paymentDetailRepository;
    }

    public Object getByPaymentId(BigInteger id) {
        List<Map<String,Object> >ids =  paymentDetailRepository.findByPaymentId(id);
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
        objectMap.put("sum",sum);
        return objectMap;
    }

    public Object createList(Map<String, Object> payload) {
        BigInteger paymentId = BigInteger.valueOf(((Number) payload.get("paymentId")).longValue());
        List<Map<String,Object>> ids = (List<Map<String, Object>>) payload.get("ids");

        List<PaymentDetail> paymentDetailList = paymentDetailRepository.findAllByPaymentId(paymentId);
        if( ! paymentDetailList.isEmpty()){
            System.out.println("aaaaaaaaaaaa");
            System.out.println("update");
            System.out.println(paymentDetailList);
            return "update";
        }

        List<ServiceDTO> serviceDTOS = serviceService.getAll();

        List<PaymentDetail> paymentDetails = new ArrayList<>();
        for (Map<String,Object> objectMap: ids) {
            if(objectMap.get("value")!= null){
                PaymentDetail paymentDetail = new PaymentDetail();
                Payment payment = new Payment();
                payment.setId(paymentId);
                paymentDetail.setPayment(payment);
                com.example.demo.Entity.Service service = new com.example.demo.Entity.Service();
                service.setId(BigInteger.valueOf(((Number) objectMap.get("id")).longValue()));
                BigDecimal value = new BigDecimal((BigInteger.valueOf(((Number) objectMap.get("value")).longValue())));
                paymentDetail.setService(service);
                BigDecimal heSo = serviceDTOS.stream()
                        .filter(x -> x.getId().equals(service.getId()))
                        .map(ServiceDTO::getServicePrice)
                        .findFirst()
                        .orElse(BigDecimal.ZERO);
                paymentDetail.setAmountToPay(heSo.multiply(value));
                paymentDetail.setStatus(Utils.ACTIVE);
                paymentDetails.add(paymentDetail);
            }
        }
        paymentDetails= paymentDetailRepository.saveAll(paymentDetails);
        return paymentDetails;
    }
}
