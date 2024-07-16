package com.example.demo.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Phong")
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
}
