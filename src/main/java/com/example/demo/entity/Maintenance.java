package com.example.demo.entity;

import com.example.demo.DTO.MaintenanceDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class Maintenance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "RoomId", nullable = false)
    private Room room;

    @Column(name = "MaintenanceRequest", nullable = false)
    private String maintenanceRequest;

    @Column(name = "MaintenanceStatus",nullable = false)
    private int maintenanceStatus;

    @Column(name = "CompanyId",nullable = false)
    private Long companyId;

    @Column(name = "Status",nullable = false)
    private int status;

    public static  Maintenance toEntity(MaintenanceDTO maintenanceDTO){
        return Maintenance.builder()
                .id(maintenanceDTO.getId())
                .room(Room.toEntity(maintenanceDTO.getRoom()))
                .maintenanceRequest(maintenanceDTO.getMaintenanceRequest())
                .maintenanceStatus(maintenanceDTO.getMaintenanceStatus())
                .companyId(maintenanceDTO.getCompanyId())
                .status(maintenanceDTO.getStatus())
                .build();
    }

    public static  MaintenanceDTO toDTO(Maintenance maintenance){
        return MaintenanceDTO.builder()
                .id(maintenance.getId())
                .room(Room.toDTO(maintenance.getRoom()))
                .maintenanceRequest(maintenance.getMaintenanceRequest())
                .maintenanceStatus(maintenance.getMaintenanceStatus())
                .companyId(maintenance.getCompanyId())
                .status(maintenance.getStatus())
                .build();
    }
}
