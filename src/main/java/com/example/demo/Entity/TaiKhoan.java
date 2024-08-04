//package com.example.demo.Entity;
//
//
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//import java.math.BigInteger;
//import java.util.Date;
//import java.util.Set;
//import java.util.UUID;
//
//@Entity
//@Data
//@AllArgsConstructor
//@NoArgsConstructor
//@Builder
//@Table(name ="TaiKhoan" )
//public class TaiKhoan {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "Id")
//    private BigInteger id;
//    @Column(name = "Ma")
//    private String ma;
//    @Column(name = "TenDangNhap")
//    private String tenDangNhap;
//    @Column(name = "MatKhau")
//    private String matKhau;
//    @Column(name = "NgayTao")
//    private Date ngayTao;
//    @Column(name = "NgaySua")
//    private Date ngaySua;
//    @Column(name = "TrangThai")
//    private Integer trangThai;
//    @Column(name = "CompanyId")
//    private BigInteger companyId;
//    @ManyToMany
//    @Column(name = "ChucVus")
//    private Set<ChucVu> chucVus;
//}