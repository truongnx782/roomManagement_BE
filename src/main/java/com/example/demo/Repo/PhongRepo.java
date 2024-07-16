package com.example.demo.Repo;

import com.example.demo.Entity.DichVu;
import com.example.demo.Entity.Phong;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigInteger;

public interface PhongRepo extends JpaRepository<Phong, BigInteger> {
    @Query(value = "SELECT p FROM Phong p WHERE " +
            "(p.tenPhong LIKE %:search% OR p.maPhong LIKE %:search% ) " +
            "AND (:status IS NULL OR p.trangThai =:status) " +
            "ORDER BY p.id DESC")
    Page<Phong> search(@Param("search") String search,
                        @Param("status") Integer status,
                        Pageable pageable);
}
