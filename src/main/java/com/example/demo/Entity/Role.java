package com.example.demo.Entity;

import com.example.demo.Util.ERole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "Name",nullable = false)
    private ERole name;
}
