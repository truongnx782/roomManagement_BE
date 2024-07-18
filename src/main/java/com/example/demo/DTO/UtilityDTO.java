package com.example.demo.DTO;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UtilityDTO {
    private BigInteger id;

    private String utilityCode;

    private String utilityName;

    private BigInteger companyId;

    private int status;


}
