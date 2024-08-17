package com.example.demo.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentDTO {
    private Long id;

    private String paymentCode;

    private ContractDTO contract;

    private BigDecimal amount;

    private LocalDate paymentDate;

    private Long previousMonthElectricity;

    private Long previousMonthWater;

    private BigDecimal rentPrice;

    private Integer paymentStatus;

    private Long companyId;

    private Integer status;



}
