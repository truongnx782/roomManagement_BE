package com.example.demo.controller;

import com.example.demo.service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/payment")
public class PaymentController {
    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/search")
    public ResponseEntity<?> search(@RequestHeader("cid") Long cid,
                                    @RequestBody Map<String, Object> payload) {
        return ResponseEntity.ok(paymentService.search(payload, cid));
    }

    @PostMapping("/payment-status")
    public ResponseEntity<?> updatePaymentStatus(@RequestHeader("cid") Long cid,
                                                 @RequestBody Map<String, Object> payload) {
        return ResponseEntity.ok(paymentService.updatePaymentStatus(payload, cid));
    }
}
