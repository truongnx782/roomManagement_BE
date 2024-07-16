package com.example.demo.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "NguoiThue")
public class NguoiThue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Long id;

    @Column(name = "MaNguoiThue")
    private String maNguoiThue;

    @Column(name = "TenNguoiThue")
    private String tenNguoiThue;

    @Column(name = "SoCCCD")
    private String soCCCD;

    @Column(name = "SoDienThoai")
    private String soDienThoai;

    @Column(name = "TrangThai")
    private int trangThai;

    @Column(name = "NgaySinhNguoiThue")
    private Date ngaySinhNguoiThue;
}
