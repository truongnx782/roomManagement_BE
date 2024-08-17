package com.example.demo.entity;

import com.example.demo.DTO.RoomDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Long id;

    @Column(name = "RoomCode",nullable = false)
    private String roomCode;

    @Column(name = "RoomName",nullable = false)
    private String roomName;

    @Column(name = "Area",nullable = false)
    private String area;

    @Column(name = "RentPrice",nullable = false)
    private BigDecimal rentPrice;

    @Column(name = "Address",nullable = false)
    private String address;

    @Column(name = "CompanyId",nullable = false)
    private Long companyId;

    @Column(name = "Status",nullable = false)
    private int status;

    @Column(name = "RentStatus",nullable = false)
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
