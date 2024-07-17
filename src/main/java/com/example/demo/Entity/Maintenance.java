package com.example.demo.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "BaoTri")
public class BaoTri {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private BigInteger id;

    @ManyToOne
    @JoinColumn(name = "IdPhong")
    private Phong phong;

    @Column(name = "YeuCauBaoTri")
    private String yeuCauBaoTri;

    @Column(name = "TrangThaiBaoTri")
    private int trangThaiBaoTri;

    @Column(name = "TrangThai")
    private int trangThai;
}
