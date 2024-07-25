package com.example.demo.Entity;

import com.example.demo.DTO.PaymentDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "Payment")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private BigInteger id;

    @Column(name = "PaymentCode")
    private String paymentCode;

    @ManyToOne
    @JoinColumn(name = "ContractId")
    private Contract contract;

    @Column(name = "Amount")
    private BigDecimal amount;

    @Column(name = "PaymentDate")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate paymentDate;

    @Column(name = "PreviousMonthElectricity")
    private BigInteger previousMonthElectricity;

    @Column(name = "PreviousMonthWater")
    private BigInteger previousMonthWater;

    @Column(name = "PaymentStatus")
    private Integer paymentStatus;

    @Column(name = "CompanyId")
    private BigInteger companyId;

    @Column(name = "Status")
    private Integer status;

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
