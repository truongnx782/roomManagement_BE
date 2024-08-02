package com.example.demo.Controller;

import com.example.demo.Service.PaymentDetailService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*",maxAge = 3600)

@RequestMapping("/payment-detail")
public class PaymentDetailController {
    private final PaymentDetailService paymentDetailService;

    public PaymentDetailController(PaymentDetailService paymentDetailService) {
        this.paymentDetailService = paymentDetailService;
    }
    @GetMapping("/paymentId/{id}")
    public ResponseEntity<?> getByPaymentId(@PathVariable("id") BigInteger id) {
        try {
            return ResponseEntity.ok(paymentDetailService.getByPaymentId(id));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Failed to retrieve Service: " + e.getMessage());
        }
    }

    @PostMapping("/create-list")
    public ResponseEntity<?> createList(@RequestBody Map<String,Object> payload)  {
        try {
            return ResponseEntity.ok(paymentDetailService.createList(payload));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Failed to retrieve Service: " + e.getMessage());
        }
    }
}
