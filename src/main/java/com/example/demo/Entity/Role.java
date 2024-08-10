package com.example.demo.Entity;

import com.example.demo.Util.ERole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Role")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private BigInteger id;

    @Enumerated(EnumType.STRING)
    @Column(name = "Name")
    private ERole name;
}
