package com.example.demo.Entity;

import com.example.demo.DTO.ContractDTO;
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
public class Contract {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Long id;

    @Column(name = "ContractCode", nullable = false)
    private String contractCode;

    @ManyToOne
    @JoinColumn(name = "RoomId", nullable = false)
    private Room room;

    @Column(name = "StartDate", nullable = false)
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate startDate;

    @Column(name = "EndDate")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    @Column(name = "RentPrice", nullable = false)
    private BigDecimal rentPrice;

    @Column(name = "Terms")
    private String terms;

    @Column(name = "CompanyId", nullable = false)
    private Long companyId;

    @Column(name = "Status", nullable = false)
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
