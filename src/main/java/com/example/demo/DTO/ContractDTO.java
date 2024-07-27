package com.example.demo.DTO;

import com.example.demo.Entity.Room;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ContractDTO {
    private BigInteger id;
    private String contractCode;
    private RoomDTO room;
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal rentPrice;
    private String terms;
    private BigInteger companyId;
    private int status;
    private List<BigInteger> customerIds;

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
