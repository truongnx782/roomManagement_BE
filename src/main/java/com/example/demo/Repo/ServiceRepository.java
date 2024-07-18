package com.example.demo.Repo;

import com.example.demo.Entity.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigInteger;
import java.util.Optional;

public interface ServiceRepository extends JpaRepository<Service, BigInteger> {

    @Query(value = "SELECT s FROM Service s WHERE " +
            "(s.serviceName LIKE %:search% OR s.serviceCode LIKE %:search% ) " +
            "AND (:status IS NULL OR s.status = :status) " +
            "ORDER BY s.id DESC")
    Page<Service> search(@Param("search") String search,
                         @Param("status") Integer status,
                         Pageable pageable);

    @Query(value = "SELECT nv FROM Service nv WHERE nv.id = (SELECT MAX(nv2.id) FROM Service nv2)")
    Optional<Service> findMaxId();
}

