package com.example.demo.Entity;

import com.example.demo.DTO.ContractDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "Contract")
public class Contract {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private BigInteger id;

    @Column(name = "ContractCode")
    private String contractCode;

    @ManyToOne
    @JoinColumn(name = "RoomId")
    private Room room;

    @Column(name = "StartDate")
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate startDate;

    @Column(name = "EndDate")
    @JsonFormat(pattern = "yyyy-MM-dd")

    private LocalDate endDate;

    @Column(name = "RentPrice")
    private BigDecimal rentPrice;

    @Column(name = "Terms")
    private String terms;

    @Column(name = "CompanyId")
    private BigInteger companyId;

    @Column(name = "Status")
    private int status;

    public static ContractDTO toDTO(Contract contract){
        return ContractDTO.builder()
                .id(contract.getId())
                .contractCode(contract.getContractCode())
                .room(Room.toDTO(contract.getRoom()))
                .startDate(contract.getStartDate())
                .endDate(contract.getEndDate())
                .rentPrice(contract.getRentPrice())
                .terms(contract.getTerms())
                .companyId(contract.getCompanyId())
                .status(contract.getStatus())
                .build();
    }

    public static Contract toEntity(ContractDTO contractDTO){
        return Contract.builder()
                .id(contractDTO.getId())
                .contractCode(contractDTO.getContractCode())
                .room(Room.toEntity(contractDTO.getRoom()))
                .startDate(contractDTO.getStartDate())
                .endDate(contractDTO.getEndDate())
                .rentPrice(contractDTO.getRentPrice())
                .terms(contractDTO.getTerms())
                .companyId(contractDTO.getCompanyId())
                .status(contractDTO.getStatus())
                .build();
    }
}
