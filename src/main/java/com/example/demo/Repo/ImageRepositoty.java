package com.example.demo.Repo;

import com.example.demo.Entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigInteger;
import java.util.List;

public interface ImageRepositoty extends JpaRepository<Image, BigInteger> {
    @Query(value = "select i from Image i where i.room.id=:roomId")
    List<Image> findAllByRoomId(@Param("roomId") BigInteger roomId);
}
