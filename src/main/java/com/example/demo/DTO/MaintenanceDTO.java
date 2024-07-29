package com.example.demo.DTO;

import com.example.demo.Entity.Room;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MaintenanceDTO {
    private BigInteger id;
    private RoomDTO room;
    private String maintenanceRequest;
    private int maintenanceStatus;
    private BigInteger companyId;
    private int status;
}
