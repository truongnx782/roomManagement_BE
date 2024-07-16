package com.example.demo.Repo;

import com.example.demo.Entity.Phong;
import com.example.demo.Entity.TienIch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigInteger;

public interface TienIchRepo extends JpaRepository<TienIch, BigInteger> {
    @Query(value = "SELECT t FROM TienIch t WHERE " +
            "(t.maTienIch LIKE %:search% OR t.tenTienIch LIKE %:search% ) " +
            "AND (:status IS NULL OR t.trangThai =:status) " +
            "ORDER BY t.id DESC")
    Page<TienIch> search(@Param("search") String search,
                       @Param("status") Integer status,
                       Pageable pageable);}
