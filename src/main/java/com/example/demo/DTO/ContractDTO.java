package com.example.demo.DTO;

import com.example.demo.DTO.DtoSecurity.RoomDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ContractDTO {
    private Long id;
    private String contractCode;
    private RoomDTO room;
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal rentPrice;
    private String terms;
    private Long companyId;
    private int status;
    private List<Long> customerIds;

    public void validateContractTO(ContractDTO contractDTO) {
        if (contractDTO.getRoom() == null) {
            throw new IllegalArgumentException("Room name cannot be empty.");
        }
        if ( contractDTO.getStartDate() == null ) {
            throw new IllegalArgumentException("Start Date cannot be empty.");
        }
        if (contractDTO.getCustomerIds() == null|| contractDTO.getCustomerIds().size()==0) {
            throw new IllegalArgumentException("Customer cannot be empty.");
        }
        if (contractDTO.getEndDate() != null && contractDTO.getEndDate().isBefore(contractDTO.getStartDate())) {
            throw new IllegalArgumentException("End date cannot be before start date.");
        }

    }

}
