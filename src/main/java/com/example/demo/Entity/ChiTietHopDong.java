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
@Table(name = "ChiTietHopDong")
public class ChiTietHopDong {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "IdHopDong")
    private HopDong hopDong;

    @ManyToOne
    @JoinColumn(name = "IdNguoiThue")
    private NguoiThue nguoiThue;

    @Column(name = "NgayThamGia")
    private Date ngayThamGia;

    @Column(name = "NgayRoiDi")
    private Date ngayRoiDi;

    @Column(name = "SoTienPhaiTra")
    private BigDecimal soTienPhaiTra;

    @Column(name = "TrangThai")
    private int trangThai;
}