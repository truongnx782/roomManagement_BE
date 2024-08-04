package com.example.demo.Repo;

import com.example.demo.Entity.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

public interface ServiceRepository extends JpaRepository<Service, BigInteger> {

    @Query(value = "SELECT s FROM Service s WHERE " +
            "(s.serviceName LIKE %:search% OR s.serviceCode LIKE %:search% ) " +
            "AND (:status IS NULL OR s.status = :status) " +
            "AND s.companyId=:cid " +
            "ORDER BY s.id DESC")
    Page<Service> search(@Param("search") String search,
                         @Param("status") Integer status,
                         @Param("cid") BigInteger cid, Pageable pageable);

    @Query(value = "SELECT nv FROM Service nv WHERE " +
            " nv.id = (SELECT MAX(nv2.id) FROM Service nv2 where " +
            " nv2.companyId=:cid) AND nv.companyId=:cid")
    Optional<Service> findMaxId(@Param("cid") BigInteger cid);

    List<Service> findAllByCompanyIdOrderByIdDesc(BigInteger cid);

    Optional<Service> findByIdAndCompanyId(BigInteger id, BigInteger cid);
}

