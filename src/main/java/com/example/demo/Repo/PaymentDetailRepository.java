package com.example.demo.Repo;

import com.example.demo.Entity.Contract;
import com.example.demo.Entity.PaymentDetail;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface PaymentDetailRepository extends JpaRepository<PaymentDetail, Long> {

@Query(value = "select  s.id as id,p.amountToPay as value "+
        " from PaymentDetail p left join Service s on p.service.id=s.id " +
        "where p.payment.id=:id and p.companyId=:cid and s.companyId=:cid")
    List<Map<String, Object>> findByPaymentIdAndCpmpanyId(@Param("id") Long id,
                                                          @Param("cid") Long cid);

    @Query("select p from PaymentDetail p where p.payment.id in :ids and p.companyId=:cid")
    List<PaymentDetail> findAllByPaymentIdsAndCompanyId(@Param("ids") List<Long> ids,
                                                        @Param("cid") Long cid);

    @Query(value = "select  p from PaymentDetail p where p.payment.id=:id and p.companyId=:cid")
    List<PaymentDetail> findAllByPaymentIdAndCompanyId(@Param("id") Long id,Long cid );

    @Modifying
    @Transactional
    @Query("DELETE FROM PaymentDetail c WHERE c.payment.id = :id and c.companyId=:cid")
    void deleteAllByPaymentIdAndCompanyId(@Param("id") Long id,
                                          @Param("cid") Long cid);

    Optional<Contract> findByPaymentIdAndCompanyId(Long id, Long cid);
}
