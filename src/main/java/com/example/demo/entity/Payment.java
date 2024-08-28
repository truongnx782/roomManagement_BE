package com.example.demo.entity;

import com.example.demo.DTO.PaymentDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Long id;

    @Column(name = "PaymentCode",nullable = false)
    private String paymentCode;

    @ManyToOne
    @JoinColumn(name = "ContractId",nullable = false)
    private Contract contract;

    @Column(name = "Amount")
    private BigDecimal amount;

    @Column(name = "PaymentDate",nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate paymentDate;

    @Column(name = "PreviousMonthElectricity")
    private Long previousMonthElectricity;

    @Column(name = "PreviousMonthWater")
    private Long previousMonthWater;

    @Column(name = "PaymentStatus",nullable = false)
    private Integer paymentStatus;

    @Column(name = "CompanyId",nullable = false)
    private Long companyId;

    @Column(name = "Status",nullable = false)
    private int status;

    public static Payment toEntity(PaymentDTO paymentDTO){
        return  Payment.builder()
                .id(paymentDTO.getId())
                .paymentCode(paymentDTO.getPaymentCode())
                .contract(Contract.toEntity(paymentDTO.getContract()))
                .amount(paymentDTO.getAmount())
                .paymentDate(paymentDTO.getPaymentDate())
                .previousMonthElectricity(paymentDTO.getPreviousMonthElectricity())
                .previousMonthWater(paymentDTO.getPreviousMonthWater())
                .paymentStatus(paymentDTO.getPaymentStatus())
                .companyId(paymentDTO.getCompanyId())
                .status(paymentDTO.getStatus())
                .build();
    }

    public static PaymentDTO toDTO(Payment payment){
        return  PaymentDTO.builder()
                .id(payment.getId())
                .paymentCode(payment.getPaymentCode())
                .contract(Contract.toDTO(payment.getContract()))
                .amount(payment.getAmount())
                .paymentDate(payment.getPaymentDate())
                .previousMonthElectricity(payment.getPreviousMonthElectricity())
                .previousMonthWater(payment.getPreviousMonthWater())
                .paymentStatus(payment.getPaymentStatus())
                .companyId(payment.getCompanyId())
                .status(payment.getStatus())
                .build();
    }

}
