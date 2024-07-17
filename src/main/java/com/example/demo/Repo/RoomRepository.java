package com.example.demo.Repo;

import com.example.demo.Entity.Room;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigInteger;

public interface PhongRepo extends JpaRepository<Room, BigInteger> {
    @Query(value = "SELECT p FROM Room p WHERE " +
            "(p.tenPhong LIKE %:search% OR p.maPhong LIKE %:search% ) " +
            "AND (:status IS NULL OR p.trangThai =:status) " +
            "ORDER BY p.id DESC")
    Page<Room> search(@Param("search") String search,
                      @Param("status") Integer status,
                      Pageable pageable);
}
