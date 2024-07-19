package com.example.demo.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ContractDetail")
public class ContractDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private BigInteger id;

    @ManyToOne
    @JoinColumn(name = "ContractId")
    private Contract contract;

    @ManyToOne
    @JoinColumn(name = "TenantId")
    private Customer customer;

    @Column(name = "JoinDate")
    private Date joinDate;

    @Column(name = "LeaveDate")
    private Date leaveDate;

    @Column(name = "AmountToPay")
    private BigDecimal amountToPay;

    @Column(name = "CompanyId")
    private BigInteger companyId;

    @Column(name = "Status")
    private int status;
}
