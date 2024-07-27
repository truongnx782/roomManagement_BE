package com.example.demo.DTO;

import com.example.demo.Entity.Contract;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentDTO {
    private BigInteger id;

    private String paymentCode;

    private ContractDTO contract;

    private BigDecimal amount;

    private LocalDate paymentDate;

    private BigInteger previousMonthElectricity;

    private BigInteger previousMonthWater;

    private BigDecimal rentPrice;

    private Integer paymentStatus;

    private BigInteger companyId;

    private Integer status;



}
