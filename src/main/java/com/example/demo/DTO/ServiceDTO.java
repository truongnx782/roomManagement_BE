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

    public void validateServiceDTO(ServiceDTO serviceDTO) {
        if (serviceDTO.getServiceName() == null || serviceDTO.getServiceName().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty.");
        }
        if (serviceDTO.getStartDate() == null) {
            throw new IllegalArgumentException("Start date cannot be null.");
        }
        if (serviceDTO.getServicePrice() == null) {
            throw new IllegalArgumentException("Service price cannot be null.");
        }
        if (serviceDTO.getStartDate().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Start date cannot be in the past.");
        }
        if (serviceDTO.getEndDate() != null && serviceDTO.getEndDate().isBefore(serviceDTO.getStartDate())) {
            throw new IllegalArgumentException("End date cannot be before start date.");
        }
    }

}
