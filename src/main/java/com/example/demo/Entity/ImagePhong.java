package com.example.demo.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ImagePhong")
public class ImagePhong {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "IdPhong")
    private Phong phong;

    @ManyToOne
    @JoinColumn(name = "IdTienIch")
    private TienIch tienIch;

    @Column(name = "TrangThai")
    private int trangThai;

    @Column(name = "DuongDan")
    private String duongDan;
}
