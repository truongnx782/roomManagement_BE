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

    public void validateRoomDTO(RoomDTO roomDTO) {
        if (roomDTO.getRoomName() == null || roomDTO.getRoomName().isEmpty()) {
            throw new IllegalArgumentException("Room name cannot be empty.");
        }
        if (roomDTO.getArea() == null || roomDTO.getArea().isEmpty()) {
            throw new IllegalArgumentException("Area cannot be empty.");
        }
        if (roomDTO.getRentPrice() == null) {
            throw new IllegalArgumentException("Rent price cannot be null.");
        }
        if (roomDTO.getAddress() == null || roomDTO.getAddress().isEmpty()) {
            throw new IllegalArgumentException("Address cannot be empty.");
        }
        if (roomDTO.getStatus() < 0) {
            throw new IllegalArgumentException("Status cannot be negative.");
        }
        if (roomDTO.getRentStatus() < 0) {
            throw new IllegalArgumentException("Rent status cannot be negative.");
        }
    }


}
