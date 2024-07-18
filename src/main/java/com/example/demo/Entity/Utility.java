package com.example.demo.Entity;

import com.example.demo.DTO.UtilityDTO;
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
@Table(name = "Utility")
public class Utility {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private BigInteger id;

    @Column(name = "UtilityName")
    private String utilityName;

    @Column(name = "Status")
    private int status;

    @Column(name = "UtilityCode")
    private String utilityCode;

    @Column(name = "CompanyId")
    private BigInteger companyId;

    public static UtilityDTO toDTO(Utility tienIch) {
        return UtilityDTO.builder()
                .id(tienIch.getId())
                .utilityCode(tienIch.getUtilityCode())
                .utilityName(tienIch.getUtilityName())
                .status(tienIch.getStatus())
                .build();
    }

    public static Utility toEntity(UtilityDTO tienIchDTO) {
        return Utility.builder()
                .id(tienIchDTO.getId())
                .utilityCode(tienIchDTO.getUtilityCode())
                .utilityName(tienIchDTO.getUtilityName())
                .status(tienIchDTO.getStatus())
                .build();
    }


}
