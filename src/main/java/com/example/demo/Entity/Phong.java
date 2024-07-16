package com.example.demo.Entity;

import com.example.demo.DTO.PhongDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Phong")
@Builder
public class Phong {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Long id;

    @Column(name = "MaPhong")
    private String maPhong;

    @Column(name = "TenPhong")
    private String tenPhong;

    @Column(name = "DienTich")
    private String dienTich;

    @Column(name = "GiaThue")
    private BigDecimal giaThue;

    @Column(name = "DiaChi")
    private String diaChi;

    @Column(name = "TrangThai")
    private int trangThai;

    @Column(name = "TrangThaiThue")
    private int trangThaiThue;


    public static PhongDTO toDTO(Phong phong) {
        if (phong == null) {
            return null;
        }
        return PhongDTO.builder()
                .id(phong.getId())
                .maPhong(phong.getMaPhong())
                .tenPhong(phong.getTenPhong())
                .dienTich(phong.getDienTich())
                .giaThue(phong.getGiaThue())
                .diaChi(phong.getDiaChi())
                .trangThai(phong.getTrangThai())
                .trangThaiThue(phong.getTrangThaiThue())
                .build();
    }

    public static Phong toEntity(PhongDTO phongDTO) {
        return Phong.builder()
                .id(phongDTO.getId())
                .maPhong(phongDTO.getMaPhong())
                .tenPhong(phongDTO.getTenPhong())
                .dienTich(phongDTO.getDienTich())
                .giaThue(phongDTO.getGiaThue())
                .diaChi(phongDTO.getDiaChi())
                .trangThai(phongDTO.getTrangThai())
                .trangThaiThue(phongDTO.getTrangThaiThue())
                .build();
    }

}
