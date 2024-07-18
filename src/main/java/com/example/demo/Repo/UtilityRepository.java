package com.example.demo.repository;

import com.example.demo.Entity.Utility;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigInteger;

public interface UtilityRepository extends JpaRepository<Utility, BigInteger> {
    @Query(value = "SELECT u FROM Utility u WHERE " +
            "(u.utilityCode LIKE %:search% OR u.utilityName LIKE %:search% ) " +
            "AND (:status IS NULL OR u.status = :status) " +
            "ORDER BY u.id DESC")
    Page<Utility> search(@Param("search") String search,
                         @Param("status") Integer status,
                         Pageable pageable);
}
