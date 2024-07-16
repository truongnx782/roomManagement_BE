package com.example.demo.Entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "DichVu")
public class DichVu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Long id;

    @Column(name = "MaDichVu")
    private String maDichVu;

    @Column(name = "TenDichVu")
    private String tenDichVu;

    @Column(name = "GiaDichVu")
    private BigDecimal giaDichVu;

    @Column(name = "NgayBatDau")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate ngayBatDau;


    @Column(name = "NgayKetThuc")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date ngayKetThuc;

    @Column(name = "TrangThai")
    private int trangThai;
}