package com.example.demo.Repo;

import com.example.demo.Entity.Image;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigInteger;
import java.util.List;

public interface ImageRepositoty extends JpaRepository<Image, BigInteger> {
    @Query(value = "select i from Image i where i.room.id=:roomId and i.companyId=:cid")
    List<Image> findAllByRoomIdAndCompanyId(@Param("roomId") BigInteger roomId,
                                            @Param("cid")BigInteger cid);

    @Modifying
    @Transactional
    @Query("DELETE FROM Image i WHERE i.id in :ids and i.companyId=:cid")
    void deleteAllByIdAndCompanyId(@Param("ids") List<BigInteger> ids,
                                   @Param("cid") BigInteger cid);
}
