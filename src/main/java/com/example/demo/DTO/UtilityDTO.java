package com.example.demo.DTO;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UtilityDTO {
    private Long id;

    private String utilityCode;

    private String utilityName;

    private Long companyId;

    private int status;


}
