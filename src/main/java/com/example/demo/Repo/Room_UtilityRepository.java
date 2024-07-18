package com.example.demo.Repo;

import com.example.demo.DTO.Room_UtilityDTO;
import com.example.demo.DTO.UtilityDTO;
import com.example.demo.Entity.Room_Utility;
import com.example.demo.Entity.Service;
import com.example.demo.Entity.Utility;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

public interface Room_UtilityRepository  extends JpaRepository<Room_Utility, BigInteger> {

    @Query(value = "select r.utility.id from Room_Utility r where r.room.id=:roomId")
    List<BigInteger> findAllUtilityIdByRoomId(@Param("roomId") BigInteger roomId);

    @Modifying
    @Transactional
    @Query("DELETE FROM Room_Utility r WHERE r.room.id = :roomId")
    void deleteByRoomId(@Param("roomId") BigInteger roomId);
}
