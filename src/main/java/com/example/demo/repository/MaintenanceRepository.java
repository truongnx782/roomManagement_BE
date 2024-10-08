package com.example.demo.repository;

import com.example.demo.entity.Maintenance;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.Optional;

public interface MaintenanceRepository extends JpaRepository<Maintenance, Long> {
    @Query(value = "SELECT u FROM Maintenance u WHERE " +
            "(u.room.roomCode LIKE %:search% OR u.room.roomName LIKE %:search% ) " +
            "AND (:status IS NULL OR u.status = :status) AND u.companyId=:cid " +
            "ORDER BY u.id DESC")
    Page<Maintenance> search(@Param("search") String search,
                             @Param("status") Integer status,
                             @Param("cid") Long cid,
                             Pageable pageable);

    Optional<Maintenance> findByIdAndCompanyId(Long id, Long cid);
}
