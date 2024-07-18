package com.example.demo.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Tenant")
public class Tenant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private BigInteger id;

    @Column(name = "TenantCode")
    private String tenantCode;

    @Column(name = "TenantName")
    private String tenantName;

    @Column(name = "IdentityNumber")
    private String identityNumber;

    @Column(name = "PhoneNumber")
    private String phoneNumber;

    @Column(name = "Status")
    private int status;

    @Column(name = "TenantBirthdate")
    private Date tenantBirthdate;

    @Column(name = "CompanyId")
    private BigInteger companyId;
}
