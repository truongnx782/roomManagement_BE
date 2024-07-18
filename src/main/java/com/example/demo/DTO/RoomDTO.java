package com.example.demo.DTO;

import com.example.demo.Entity.Company;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.BigInteger;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoomDTO {
    private BigInteger id;

    private String roomCode;

    private String roomName;

    private String area;

    private BigDecimal rentPrice;

    private String address;

    private BigInteger companyId;

    private int status;

    private int rentStatus;

}
