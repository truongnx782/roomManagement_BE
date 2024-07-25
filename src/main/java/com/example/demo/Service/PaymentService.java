package com.example.demo.Service;

import com.example.demo.DTO.PaymentDTO;
import com.example.demo.Entity.Contract;
import com.example.demo.Entity.Payment;
import com.example.demo.Repo.PaymentRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.Map;
import java.util.Optional;

@Service
public class PaymentService {
    private final PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public Page<PaymentDTO> search(Map<String, Object> payload) {
        int page = (int) payload.getOrDefault("page", 0);
        int size = (int) payload.getOrDefault("size", 5);
        String search = (String) payload.getOrDefault("search", "");
        Integer paymentStatus = (Integer) payload.get("paymentStatus");
        Pageable pageable = PageRequest.of(page, size);
        Page<Payment> data = paymentRepository.search(search, paymentStatus, pageable);
        return data.map(Payment::toDTO);
    }

    public PaymentDTO updatePaymentStatus(Map<String, Object> payload) {
        Integer paymentStatus = (Integer) payload.get("checked");
        BigInteger paymentId = BigInteger.valueOf(((Number) payload.get("id")).longValue());
        Optional<Payment> optionalPayment = paymentRepository.findById(paymentId);
        if (!optionalPayment.isPresent()) {
            throw new IllegalArgumentException("contract not found");
        }
        Payment payment = optionalPayment.get();
        payment.setPaymentStatus(paymentStatus);
        payment = paymentRepository.save(payment);
        return Payment.toDTO(payment);
    }
}
