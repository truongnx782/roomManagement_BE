package com.example.demo.Repo;
import com.example.demo.Entity.Room;
import com.example.demo.Entity.Utility;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

public interface UtilityRepository extends JpaRepository<Utility, BigInteger> {
    @Query(value = "SELECT u FROM Utility u WHERE " +
            "(u.utilityCode LIKE %:search% OR u.utilityName LIKE %:search% ) " +
            "AND (:status IS NULL OR u.status = :status) " +
            "AND u.companyId=:cid " +
            "ORDER BY u.id DESC")
    Page<Utility> search(@Param("search") String search,
                         @Param("status") Integer status,
                         @Param("cid") BigInteger cid,
                         Pageable pageable);

    @Query(value = "SELECT nv FROM Utility nv WHERE " +
            "nv.id = (SELECT MAX(nv2.id) FROM Utility nv2 where nv2.companyId=:cid) " +
            "AND nv.companyId=:cid" )
    Optional<Utility> findMaxIdByCompanyId(@Param("cid") BigInteger cid);

    List<Utility> findAllByCompanyIdOrderByIdDesc(BigInteger cid);

    Optional<Utility> findByIdAndCompanyId(BigInteger id, BigInteger cid);

    Optional<Utility> findByUtilityNameAndCompanyId(String utilityName, BigInteger companyId);
}
