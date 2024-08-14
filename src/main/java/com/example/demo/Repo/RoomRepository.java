package com.example.demo.Repo;

import com.example.demo.Entity.Room;
import com.example.demo.Entity.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.List;
import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room, Long> {
    @Query(value = "SELECT r FROM Room r WHERE " +
            "(r.roomName LIKE %:search% OR r.roomCode LIKE %:search% ) " +
            "AND (:status IS NULL OR r.status = :status) " +
            "AND r.companyId=:cid " +
            "ORDER BY r.id DESC")
    Page<Room> search(@Param("search") String search,
                      @Param("status") Integer status,
                      @Param("cid") Long cid,
                      Pageable pageable);

    @Query(value = "SELECT nv FROM Room nv " +
            "WHERE nv.id = (SELECT MAX(nv2.id) FROM Room nv2 WHERE  nv2.companyId=:cid) " +
            "AND  nv.companyId=:cid")
    Optional<Room> findMaxIdByCompanyId(@Param("cid") Long cid);

    List<Room> findAllByCompanyIdOrderByIdDesc(Long cid);

    Optional<Room> findByIdAndCompanyId(Long id, Long cid);


    Optional<Room> findByRoomNameAndCompanyId(String roomName, Long cid);
}

