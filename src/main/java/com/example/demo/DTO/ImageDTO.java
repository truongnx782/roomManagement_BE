package com.example.demo.DTO;

import com.example.demo.Entity.Room;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImageDTO {
    private BigInteger id;

    private Room room;

    private BigInteger companyId;

    private String url;

    private int status;

}
