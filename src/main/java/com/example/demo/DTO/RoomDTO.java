package com.example.demo.DTO;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.BigInteger;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PhongDTO {
    private BigInteger id;
    private String maPhong;
    private String tenPhong;
    private String dienTich;
    private BigDecimal giaThue;
    private String diaChi;
    private int trangThai;
    private int trangThaiThue;

}
