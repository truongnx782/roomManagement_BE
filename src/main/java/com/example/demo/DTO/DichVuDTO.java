package com.example.demo.DTO;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DichVuDTO {
    private Long id;
    private String maDichVu;
    private String tenDichVu;
    private BigDecimal giaDichVu;
    private LocalDate ngayBatDau;
    private Date ngayKetThuc;
    private int trangThai;

}
