package com.example.demo.DTO;

import com.example.demo.Entity.Room;
import com.example.demo.Entity.Utility;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateRoom_UtilityDTO {
    private BigInteger id;
    private BigInteger room;
    private List<BigInteger> utilitys;
    private BigInteger companyId;
    private int status;

}
