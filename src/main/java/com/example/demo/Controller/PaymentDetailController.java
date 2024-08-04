package com.example.demo.Controller;

import com.example.demo.Service.PaymentDetailService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.Map;

@RestController
@RequestMapping("/payment-detail")
public class PaymentDetailController {
    private final PaymentDetailService paymentDetailService;

    public PaymentDetailController(PaymentDetailService paymentDetailService) {
        this.paymentDetailService = paymentDetailService;
    }

    @GetMapping("/paymentId/{id}")
    public ResponseEntity<?> getByPaymentId(@RequestHeader("cid") BigInteger cid,
                                            @PathVariable("id") BigInteger id) {
        return ResponseEntity.ok(paymentDetailService.getByPaymentId(id, cid));
    }

    @PostMapping("/create-list")
    public ResponseEntity<?> createList(@RequestHeader("cid") BigInteger cid,
                                        @RequestBody Map<String, Object> payload) {
        return ResponseEntity.ok(paymentDetailService.createList(payload, cid));
    }
}
