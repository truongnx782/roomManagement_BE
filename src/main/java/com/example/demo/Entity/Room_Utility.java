package com.example.demo.Entity;

import com.example.demo.DTO.Room_UtilityDTO;
import com.example.demo.DTO.ServiceDTO;
import com.example.demo.DTO.UtilityDTO;
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
@Table(name = "Room_Utility")
@Builder
public class Room_Utility {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private BigInteger id;


    @OneToOne
    @JoinColumn(name = "RoomId")
    private Room room;

    @ManyToOne
    @JoinColumn(name = "UtilityId")
    private Utility utility;

    @Column(name = "CompanyId")
    private BigInteger companyId;


    @Column(name = "Status")
    private int status;

    public static Room_UtilityDTO toDTO(Room_Utility room_utility) {
        return Room_UtilityDTO.builder()
                .id(room_utility.getId())
                .room(room_utility.getRoom())
                .utility(room_utility.getUtility())
                .status(room_utility.getStatus())
                .build();
    }

    public static Room_Utility toEntity(Room_UtilityDTO room_utilityDTO) {
        return Room_Utility.builder()
                .id(room_utilityDTO.getId())
                .room(room_utilityDTO.getRoom())
                .utility(room_utilityDTO.getUtility())
                .status(room_utilityDTO.getStatus())
                .build();
    }

}
