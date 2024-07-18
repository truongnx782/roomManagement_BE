package com.example.demo.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Contract")
public class Contract {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private BigInteger id;

    @Column(name = "ContractCode")
    private String contractCode;

    @ManyToOne
    @JoinColumn(name = "RoomId")
    private Room room;

    @Column(name = "StartDate")
    private Date startDate;

    @Column(name = "EndDate")
    private Date endDate;

    @Column(name = "RentPrice")
    private BigDecimal rentPrice;

    @Column(name = "Terms")
    private String terms;

    @Column(name = "CompanyId")
    private BigInteger companyId;

    @Column(name = "Status")
    private int status;
}
