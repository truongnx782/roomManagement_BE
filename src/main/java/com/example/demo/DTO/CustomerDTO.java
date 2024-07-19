package com.example.demo.DTO;

import jakarta.persistence.Column;
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
@Builder
public class CustomerDTO {
    private BigInteger id;

    private String customerCode;

    private String customerName;

    private String identityNumber;

    private String phoneNumber;

    private int status;

    private LocalDate birthdate;

    private BigInteger companyId;
}
