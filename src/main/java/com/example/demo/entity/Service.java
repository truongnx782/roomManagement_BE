package com.example.demo.entity;


import com.example.demo.DTO.ServiceDTO;
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
public class Service {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Long id;

    @Column(name = "ServiceName",nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private String serviceName;

    @Column(name = "StartDate",nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @Column(name = "EndDate")
    private LocalDate endDate;

    @Column(name = "Status",nullable = false)
    private int status;

    @Column(name = "ServicePrice",nullable = false)
    private BigDecimal servicePrice;

    @Column(name = "ServiceCode",nullable = false)
    private String serviceCode;

    @Column(name = "CompanyId",nullable = false)
    private Long companyId;

    public static ServiceDTO toDTO(Service service) {
        return ServiceDTO.builder()
                .id(service.getId())
                .serviceCode(service.getServiceCode())
                .serviceName(service.getServiceName())
                .servicePrice(service.getServicePrice())
                .startDate(service.getStartDate())
                .endDate(service.getEndDate())
                .status(service.getStatus())
                .build();
    }

    public static Service toEntity(ServiceDTO serviceDTO) {
        return Service.builder()
                .id(serviceDTO.getId())
                .serviceCode(serviceDTO.getServiceCode())
                .serviceName(serviceDTO.getServiceName())
                .servicePrice(serviceDTO.getServicePrice())
                .startDate(serviceDTO.getStartDate())
                .endDate(serviceDTO.getEndDate())
                .status(serviceDTO.getStatus())
                .build();
    }

}