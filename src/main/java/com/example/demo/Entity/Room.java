package com.example.demo.Entity;

import com.example.demo.DTO.RoomDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.BigInteger;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Room")
@Builder
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private BigInteger id;

    @Column(name = "RoomCode")
    private String roomCode;

    @Column(name = "RoomName")
    private String roomName;

    @Column(name = "Area")
    private String area;

    @Column(name = "RentPrice")
    private BigDecimal rentPrice;

    @Column(name = "Address")
    private String address;

    @Column(name = "CompanyId")
    private BigInteger companyId;

    @Column(name = "Status")
    private int status;

    @Column(name = "RentStatus")
    private int rentStatus;

    public static RoomDTO toDTO(Room phong) {
        return RoomDTO.builder()
                .id(phong.getId())
                .roomCode(phong.getRoomCode())
                .roomName(phong.getRoomName())
                .area(phong.getArea())
                .rentPrice(phong.getRentPrice())
                .address(phong.getAddress())
                .status(phong.getStatus())
                .rentStatus(phong.getRentStatus())
                .companyId(phong.getCompanyId())
                .build();
    }

    public static Room toEntity(RoomDTO phongDTO) {
        return Room.builder()
                .id(phongDTO.getId())
                .roomCode(phongDTO.getRoomCode())
                .roomName(phongDTO.getRoomName())
                .area(phongDTO.getArea())
                .rentPrice(phongDTO.getRentPrice())
                .address(phongDTO.getAddress())
                .status(phongDTO.getStatus())
                .rentStatus(phongDTO.getRentStatus())
                .build();
    }


}
