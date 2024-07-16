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
@Table(name = "ThanhToan")
public class ThanhToan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "IdHopDong")
    private HopDong hopDong;

    @Column(name = "SoTien")
    private BigDecimal soTien;

    @Column(name = "NgayThanhToan")
    private Date ngayThanhToan;

    @Column(name = "TrangThai")
    private String trangThai;

    @Column(name = "SoDienThangTruoc")
    private Long soDienThangTruoc;

    @Column(name = "SoDienThangNay")
    private Long soDienThangNay;

    @Column(name = "SoNuocThangTruoc")
    private Long soNuocThangTruoc;

    @Column(name = "SoNuocThangNay")
    private Long soNuocThangNay;
}