package com.example.demo.DTO;

import com.example.demo.Entity.Room;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImageDTO {
    private Long id;

    private RoomDTO room;

    private Long companyId;

    private String url;

    private int status;

}
