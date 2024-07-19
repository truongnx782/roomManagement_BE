package com.example.demo.Repo;

import com.example.demo.Entity.Customer;
import com.example.demo.Entity.Utility;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigInteger;
import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, BigInteger> {
    @Query(value = "SELECT u FROM Customer u WHERE " +
            "(u.customerCode LIKE %:search% OR u.customerName LIKE %:search% OR  u.phoneNumber LIKE %:search%  ) " +
            "AND (:status IS NULL OR u.status = :status) " +
            "ORDER BY u.id DESC")
    Page<Customer> search(@Param("search") String search,
                         @Param("status") Integer status,
                         Pageable pageable);
    @Query(value = "SELECT nv FROM Customer nv WHERE nv.id = (SELECT MAX(nv2.id) FROM Room nv2)")
    Optional<Customer> findMaxId();
}
