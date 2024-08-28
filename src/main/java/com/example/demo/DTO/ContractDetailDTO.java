package com.example.demo.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;



@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ContractDetailDTO {
    private Long id;
    private ContractDTO contract;
    private CustomerDTO customer;
    private Long companyId;
    private int status;
}
