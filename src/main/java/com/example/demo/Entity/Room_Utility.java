package com.example.demo.Entity;

import com.example.demo.DTO.Room_UtilityDTO;
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
public class Room_Utility {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "RoomId",nullable = false)
    private Room room;

    @ManyToOne
    @JoinColumn(name = "UtilityId",nullable = false)
    private Utility utility;

    @Column(name = "CompanyId",nullable = false)
    private Long companyId;


    @Column(name = "Status",nullable = false)
    private int status;

    public static Room_UtilityDTO toDTO(Room_Utility room_utility) {
        return Room_UtilityDTO.builder()
                .id(room_utility.getId())
                .room(Room.toDTO(room_utility.getRoom()))
                .utility(Utility.toDTO(room_utility.getUtility()))
                .status(room_utility.getStatus())
                .build();
    }

    public static Room_Utility toEntity(Room_UtilityDTO room_utilityDTO) {
        return Room_Utility.builder()
                .id(room_utilityDTO.getId())
                .room(Room.toEntity(room_utilityDTO.getRoom()))
                .utility(Utility.toEntity(room_utilityDTO.getUtility()))
                .status(room_utilityDTO.getStatus())
                .build();
    }

}
