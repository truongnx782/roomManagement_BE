package com.example.demo.repository;

import com.example.demo.entity.Image;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.List;

public interface ImageRepositoty extends JpaRepository<Image, Long> {
    @Query(value = "select i from Image i where i.room.id=:roomId and i.companyId=:cid")
    List<Image> findAllByRoomIdAndCompanyId(@Param("roomId") Long roomId,
                                            @Param("cid")Long cid);

    @Modifying
    @Transactional
    @Query("DELETE FROM Image i WHERE i.id in :ids and i.companyId=:cid")
    void deleteAllByIdAndCompanyId(@Param("ids") List<Long> ids,
                                   @Param("cid") Long cid);
}
