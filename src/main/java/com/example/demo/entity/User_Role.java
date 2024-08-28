package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class User_Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "UserId", nullable = false)
    private User userId;

    @ManyToOne
    @JoinColumn(name = "RoleId", nullable = false)
    private Role roleId;

    @Column(name = "Status", nullable = false)
    private int status;

    @Column(name = "CompanyId", nullable = false)
    private Long companyId;
}
