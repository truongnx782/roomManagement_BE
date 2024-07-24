package com.example.demo.DTO;

import com.example.demo.Entity.Room;
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
    private String terms;
    private BigInteger companyId;
    private int status;
    private List<BigInteger> customerIds;

}
