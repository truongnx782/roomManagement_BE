package com.example.demo.Entity;

import com.example.demo.DTO.CustomerDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "Customer")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private BigInteger id;

    @Column(name = "CustomerCode")
    private String customerCode;

    @Column(name = "CustomerName")
    private String customerName;

    @Column(name = "IdentityNumber")
    private String identityNumber;

    @Column(name = "PhoneNumber")
    private String phoneNumber;

    @Column(name = "Status")
    private int status;

    @Column(name = "Birthdate")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthdate;

    @Column(name = "CompanyId")
    private BigInteger companyId;

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
