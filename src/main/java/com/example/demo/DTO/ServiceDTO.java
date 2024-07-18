package com.example.demo.DTO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Builder
public class ServiceDTO {
    private BigInteger id;

    private String serviceName;

    private LocalDate startDate;

    private LocalDate endDate;

    private int status;

    private BigDecimal servicePrice;

    private String serviceCode;

    private BigInteger companyId;

}
