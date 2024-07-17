package com.example.demo.Entity;

import com.example.demo.DTO.DichVuDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "DichVu")
public class DichVu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private BigInteger id;

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
    private LocalDate ngayKetThuc;

    @Column(name = "TrangThai")
    private int trangThai;

    public static DichVuDTO toDTO(DichVu dichVu) {
        return DichVuDTO.builder()
                .id(dichVu.getId())
                .maDichVu(dichVu.getMaDichVu())
                .tenDichVu(dichVu.getTenDichVu())
                .giaDichVu(dichVu.getGiaDichVu())
                .ngayBatDau(dichVu.getNgayBatDau())
                .ngayKetThuc(dichVu.getNgayKetThuc())
                .trangThai(dichVu.getTrangThai())
                .build();
    }

    public static DichVu toEntity(DichVuDTO dichVuDTO) {
        return DichVu.builder()
                .id(dichVuDTO.getId())
                .maDichVu(dichVuDTO.getMaDichVu())
                .tenDichVu(dichVuDTO.getTenDichVu())
                .giaDichVu(dichVuDTO.getGiaDichVu())
                .ngayBatDau(dichVuDTO.getNgayBatDau())
                .ngayKetThuc(dichVuDTO.getNgayKetThuc())
                .trangThai(dichVuDTO.getTrangThai())
                .build();
    }
}