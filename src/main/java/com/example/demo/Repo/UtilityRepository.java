package com.example.demo.Repo;

import com.example.demo.Entity.Utility;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigInteger;

public interface TienIchRepo extends JpaRepository<Utility, BigInteger> {
    @Query(value = "SELECT t FROM Utility t WHERE " +
            "(t.maTienIch LIKE %:search% OR t.tenTienIch LIKE %:search% ) " +
            "AND (:status IS NULL OR t.trangThai =:status) " +
            "ORDER BY t.id DESC")
    Page<Utility> search(@Param("search") String search,
                         @Param("status") Integer status,
                         Pageable pageable);}
