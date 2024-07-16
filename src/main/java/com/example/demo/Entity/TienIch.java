package com.example.demo.Entity;

import com.example.demo.DTO.PhongDTO;
import com.example.demo.DTO.TienIchDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "TienIch")
public class TienIch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private BigInteger id;

    @Column(name = "MaTienIch")
    private String maTienIch;

    @Column(name = "TenTienIch")
    private String tenTienIch;

    @Column(name = "TrangThai")
    private int trangThai;

    public static TienIchDTO toDTO(TienIch tienIch) {
        return TienIchDTO.builder()
                .id(tienIch.getId())
                .maTienIch(tienIch.getMaTienIch())
                .tenTienIch(tienIch.getTenTienIch())
                .trangThai(tienIch.getTrangThai())
                .build();
    }
    public static TienIch toEntity(TienIchDTO tienIchDTO) {
        return TienIch.builder()
                .id(tienIchDTO.getId())
                .maTienIch(tienIchDTO.getMaTienIch())
                .tenTienIch(tienIchDTO.getTenTienIch())
                .trangThai(tienIchDTO.getTrangThai())
                .build();
    }

}
