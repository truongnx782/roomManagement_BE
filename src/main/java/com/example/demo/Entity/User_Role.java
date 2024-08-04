package com.example.demo.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "User_Role")
@Builder
public class User_Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private BigInteger id;

    @OneToOne
    @JoinColumn(name = "UserId")
    private User userId;

    @OneToOne
    @JoinColumn(name = "RoleId")
    private Role roleId;

    @Column(name = "Status")
    private Integer status;

    @Column(name = "CompanyId")
    private BigInteger companyId;
}
