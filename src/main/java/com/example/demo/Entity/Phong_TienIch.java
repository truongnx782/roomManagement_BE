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
@Table(name = "Phong_TienIch")
@Builder
public class Phong_TienIch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private BigInteger id;

    @ManyToOne
    @JoinColumn(name = "IdTienIch")
    private TienIch tienIch;

    @ManyToOne
    @JoinColumn(name = "IdPhong")
    private Phong phong;

    @Column(name = "TrangThai")
    private int trangThai;

}
