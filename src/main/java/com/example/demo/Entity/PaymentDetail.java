package com.example.demo.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.BigInteger;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "PaymentDetail")
public class PaymentDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private BigInteger id;

    @ManyToOne
    @JoinColumn(name = "ServiceId")
    private Service service;

    @Column(name = "AmountToPay")
    private BigDecimal amountToPay;

    @Column(name = "CompanyId")
    private BigInteger companyId;

    @Column(name = "Status")
    private Integer status;

    @ManyToOne
    @JoinColumn(name = "PaymentId")
    private Payment payment;
}
