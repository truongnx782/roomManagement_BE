package com.example.demo.Entity;

import com.example.demo.DTO.ContractDetailDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "ContractDetail")
public class ContractDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private BigInteger id;

    @ManyToOne
    @JoinColumn(name = "ContractId")
    private Contract contract;

    @ManyToOne
    @JoinColumn(name = "CustomerId")
    private Customer customer;

    @Column(name = "CompanyId")
    private BigInteger companyId;

    @Column(name = "Status")
    private int status;

    public static ContractDetail toEntity(ContractDetailDTO contractDetailDTO){
       return ContractDetail.builder()
                .id(contractDetailDTO.getId())
                .contract(Contract.toEntity(contractDetailDTO.getContract()))
                .customer(Customer.toEntity(contractDetailDTO.getCustomer()))
                .companyId(contractDetailDTO.getCompanyId())
                .status(contractDetailDTO.getStatus())
                .build();
    }

    public static ContractDetailDTO toDTO(ContractDetail contractDetail){
        return ContractDetailDTO.builder()
                .id(contractDetail.getId())
                .contract(Contract.toDTO(contractDetail.getContract()))
                .customer(Customer.toDTO(contractDetail.getCustomer()))
                .companyId(contractDetail.getCompanyId())
                .status(contractDetail.getStatus())
                .build();
    }
}
