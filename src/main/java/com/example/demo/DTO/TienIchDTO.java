package com.example.demo.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TienIchDTO {
    private BigInteger id;
    private String maTienIch;
    private String tenTienIch;
    private int trangThai;

}
