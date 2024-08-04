package com.example.demo.Repo;

import com.example.demo.Entity.Contract;
import com.example.demo.Entity.Customer;
import com.example.demo.Entity.Room;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigInteger;
import java.util.Optional;

public interface ContractRepository extends JpaRepository<Contract, BigInteger> {
    @Query(value = "SELECT u FROM Contract u WHERE " +
            "(u.contractCode LIKE %:search% OR u.room.roomName LIKE %:search% OR  u.room.roomCode LIKE %:search%  ) " +
            "AND (:status IS NULL OR u.status = :status) AND u.companyId=:cid " +
            "ORDER BY u.id DESC")
    Page<Contract> search(@Param("search") String search,
                          @Param("status") Integer status,
                          @Param("cid") String cid, Pageable pageable);

    @Query(value = "SELECT nv FROM Contract nv " +
            "WHERE nv.id = (SELECT MAX(nv2.id) FROM Contract nv2 WHERE nv2.companyId=:cid) " +
            "AND nv.companyId=:cid")
    Optional<Contract> findMaxIdAndCompanyId(BigInteger cid);

    @Query(value = "select c.room from Contract  c where c.id=:id and c.companyId=:cid")
    Optional<Room> findRoomByContractIdAndCompanyId(@Param("id") BigInteger id,
                                                    @Param("cid")BigInteger cid);

    Optional<Contract> findByIdAndCompanyId(BigInteger id, BigInteger cid);
}
