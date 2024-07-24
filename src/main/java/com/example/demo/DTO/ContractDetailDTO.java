package com.example.demo.DTO;

import com.example.demo.Entity.Contract;
import com.example.demo.Entity.Customer;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ContractDetailDTO {
    private BigInteger id;
    private ContractDTO contract;
    private CustomerDTO customer;
    private BigInteger companyId;
    private int status;
}
