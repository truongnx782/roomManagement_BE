package com.example.demo.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "HopDong")
public class HopDong {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Long id;

    @Column(name = "MaHopDong")
    private String maHopDong;

    @ManyToOne
    @JoinColumn(name = "IdPhong")
    private Phong phong;

    @Column(name = "NgayBatDau")
    private Date ngayBatDau;

    @Column(name = "NgayKetThuc")
    private Date ngayKetThuc;

    @Column(name = "GiaThue")
    private BigDecimal giaThue;

    @Column(name = "DieuKhoan")
    private String dieuKhoan;

    @Column(name = "TrangThai")
    private int trangThai;
}