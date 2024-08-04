package com.example.demo.Controller;

import com.example.demo.Service.PaymentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.Map;

@RestController
@RequestMapping("/payment")
@CrossOrigin(origins = "*",maxAge = 3600)

public class PaymentController {
    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/search")
    public ResponseEntity<?> search(@RequestHeader("cid") BigInteger cid,
                                    @RequestBody Map<String, Object> payload) {
        try {
            return ResponseEntity.ok(paymentService.search(payload,cid));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to search for payment: " + e.getMessage());
        }
    }

    @PostMapping("/payment-status")
    public ResponseEntity<?> updatePaymentStatus(@RequestHeader("cid")BigInteger cid,
                                                 @RequestBody Map<String, Object> payload) {
        try {
            return ResponseEntity.ok(paymentService.updatePaymentStatus(payload,cid));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to search for payment: " + e.getMessage());
        }
    }
}
