package com.example.demo.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Long id;

    @Column(name = "CompanyCode", nullable = false)
    private String companyCode;

    @Column(name = "CompanyName", nullable = false)
    private String companyName;

    @Column(name = "Address")
    private String address;

    @Column(name = "Status", nullable = false)
    private int status;
}
