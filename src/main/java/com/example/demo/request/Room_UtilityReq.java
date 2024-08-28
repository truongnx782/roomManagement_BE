package com.example.demo.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Room_UtilityReq {
    private Long id;
    private Long room;
    private List<Long> utilitys;
    private Long companyId;
    private int status;

}
