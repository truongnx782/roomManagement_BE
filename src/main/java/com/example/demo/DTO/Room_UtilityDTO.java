package com.example.demo.DTO;

import com.example.demo.Entity.Room;
import com.example.demo.Entity.Utility;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Room_UtilityDTO {
    private Long id;
    private RoomDTO room;
    private UtilityDTO utility;
    private Long companyId;
    private int status;

}
