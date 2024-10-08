package com.example.demo.repository;
import com.example.demo.entity.Utility;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.List;
import java.util.Optional;

public interface UtilityRepository extends JpaRepository<Utility, Long> {
    @Query(value = "SELECT u FROM Utility u WHERE " +
            "(u.utilityCode LIKE %:search% OR u.utilityName LIKE %:search% ) " +
            "AND (:status IS NULL OR u.status = :status) " +
            "AND u.companyId=:cid " +
            "ORDER BY u.id DESC")
    Page<Utility> search(@Param("search") String search,
                         @Param("status") Integer status,
                         @Param("cid") Long cid,
                         Pageable pageable);

    @Query(value = "SELECT nv FROM Utility nv WHERE " +
            "nv.id = (SELECT MAX(nv2.id) FROM Utility nv2 where nv2.companyId=:cid) " +
            "AND nv.companyId=:cid" )
    Optional<Utility> findMaxIdByCompanyId(@Param("cid") Long cid);

    List<Utility> findAllByCompanyIdOrderByIdDesc(Long cid);

    Optional<Utility> findByIdAndCompanyId(Long id, Long cid);

    Optional<Utility> findByUtilityNameAndCompanyId(String utilityName, Long companyId);
}
