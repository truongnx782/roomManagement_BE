package com.example.demo.DTO;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PhongDTO {
    private Long id;
    private String maPhong;
    private String tenPhong;
    private String dienTich;
    private BigDecimal giaThue;
    private String diaChi;
    private int trangThai;
    private int trangThaiThue;
}
