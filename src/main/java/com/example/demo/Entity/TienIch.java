package com.example.demo.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "TienIch")
public class TienIch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Long id;

    @Column(name = "TenTienIch")
    private String tenTienIch;

    @Column(name = "TrangThai")
    private int trangThai;

    @ManyToOne
    @JoinColumn(name = "IdPhong")
    private Phong phong;
}
