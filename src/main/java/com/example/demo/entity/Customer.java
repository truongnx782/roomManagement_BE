package com.example.demo.entity;

import com.example.demo.DTO.CustomerDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Long id;

    @Column(name = "CustomerCode", nullable = false)
    private String customerCode;

    @Column(name = "CustomerName", nullable = false)
    private String customerName;

    @Column(name = "IdentityNumber", nullable = false)
    private String identityNumber;

    @Column(name = "PhoneNumber", nullable = false)
    private String phoneNumber;

    @Column(name = "Birthdate", nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthdate;

    @Column(name = "CompanyId", nullable = false)
    private Long companyId;

    @Column(name = "Status", nullable = false)
    private int status;

    public static CustomerDTO toDTO(Customer customer) {
        return CustomerDTO.builder()
                .id(customer.getId())
                .customerCode(customer.getCustomerCode())
                .customerName(customer.getCustomerName())
                .identityNumber(customer.getIdentityNumber())
                .phoneNumber(customer.getPhoneNumber())
                .status(customer.getStatus())
                .birthdate(customer.getBirthdate())
                .companyId(customer.getCompanyId())
                .build();
    }
    public static Customer toEntity(CustomerDTO customerDTO) {
        return Customer.builder()
                .id(customerDTO.getId())
                .customerCode(customerDTO.getCustomerCode())
                .customerName(customerDTO.getCustomerName())
                .identityNumber(customerDTO.getIdentityNumber())
                .phoneNumber(customerDTO.getPhoneNumber())
                .status(customerDTO.getStatus())
                .birthdate(customerDTO.getBirthdate())
                .companyId(customerDTO.getCompanyId())
                .build();
    }
}

