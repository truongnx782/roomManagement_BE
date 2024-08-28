package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class PaymentDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ServiceId",nullable = false)
    private Service service;

    @Column(name = "AmountToPay",nullable = false)
    private BigDecimal amountToPay;

    @Column(name = "CompanyId",nullable = false)
    private Long companyId;

    @Column(name = "Status",nullable = false)
    private int status;

    @ManyToOne
    @JoinColumn(name = "PaymentId",nullable = false)
    private Payment payment;
}
