package com.example.demo.Entity;

import com.example.demo.DTO.MaintenanceDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "Maintenance")
public class Maintenance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private BigInteger id;

    @ManyToOne
    @JoinColumn(name = "RoomId")
    private Room room;

    @Column(name = "MaintenanceRequest")
    private String maintenanceRequest;

    @Column(name = "MaintenanceStatus")
    private int maintenanceStatus;

    @Column(name = "CompanyId")
    private BigInteger companyId;

    @Column(name = "Status")
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
