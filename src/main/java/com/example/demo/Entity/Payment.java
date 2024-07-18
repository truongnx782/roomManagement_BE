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
@Table(name = "Payment")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private BigInteger id;

    @ManyToOne
    @JoinColumn(name = "ContractId")
    private Contract contract;

    @Column(name = "Amount")
    private BigDecimal amount;

    @Column(name = "PaymentDate")
    private Date paymentDate;

    @Column(name = "Status")
    private String status;

    @Column(name = "PreviousMonthElectricity")
    private BigInteger previousMonthElectricity;

    @Column(name = "CurrentMonthElectricity")
    private BigInteger currentMonthElectricity;

    @Column(name = "PreviousMonthWater")
    private BigInteger previousMonthWater;

    @Column(name = "CompanyId")
    private BigInteger companyId;

    @Column(name = "CurrentMonthWater")
    private BigInteger currentMonthWater;
}
