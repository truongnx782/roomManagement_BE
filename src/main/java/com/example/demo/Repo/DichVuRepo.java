package com.example.demo.Repo;

import com.example.demo.Entity.DichVu;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigInteger;

public interface DichVuRepo extends JpaRepository<DichVu, BigInteger> {

    @Query(value = "SELECT dv FROM DichVu dv WHERE " +
            "(dv.tenDichVu LIKE %:search% OR dv.maDichVu LIKE %:search% ) " +
            "AND (:status IS NULL OR dv.trangThai =:status) " +
            "ORDER BY dv.id DESC")
    Page<DichVu> search(@Param("search") String search,
                        @Param("status") Integer status,
                        Pageable pageable);

}
