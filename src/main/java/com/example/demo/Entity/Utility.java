package com.example.demo.Entity;

import com.example.demo.DTO.UtilityDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class Utility {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Long id;

    @Column(name = "UtilityName",nullable = false)
    private String utilityName;

    @Column(name = "Status",nullable = false)
    private int status;

    @Column(name = "UtilityCode",nullable = false)
    private String utilityCode;

    @Column(name = "CompanyId",nullable = false)
    private Long companyId;

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
