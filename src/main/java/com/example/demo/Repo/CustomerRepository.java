package com.example.demo.Repo;

import com.example.demo.Entity.Customer;
import com.example.demo.Entity.Utility;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;


import java.util.List;
import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    @Query(value = "SELECT u FROM Customer u WHERE " +
            "(u.customerCode LIKE %:search% OR u.customerName LIKE %:search% OR  u.phoneNumber LIKE %:search%  ) " +
            "AND (:status IS NULL OR u.status = :status) AND u.companyId=:cid " +
            "ORDER BY u.id DESC")
    Page<Customer> search(@Param("search") String search,
                          @Param("status") Integer status,
                          @Param("cid") Long cid,
                          Pageable pageable);

    @Query(value = "SELECT nv FROM Customer nv WHERE" +
            " nv.id = (SELECT MAX(nv2.id) FROM Customer nv2 WHERE nv2.companyId=:cid) " +
            " AND  nv.companyId=:cid")
    Optional<Customer> findMaxIdByCompanyId(@Param("cid") Long cid);

    List<Customer> findAllByCompanyIdOrderByIdDesc(Long cid);

    Optional<Customer> findByIdAndCompanyId(Long id, Long cid);
}
