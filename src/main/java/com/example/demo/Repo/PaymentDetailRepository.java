package com.example.demo.Repo;

import com.example.demo.Entity.PaymentDetail;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

public interface PaymentDetailRepository extends JpaRepository<PaymentDetail, BigInteger> {

@Query(value = "select  s.id as id,p.amountToPay as value "+
        " from PaymentDetail p left join Service s on p.service.id=s.id " +
        "where p.payment.id=:id and p.companyId=:cid and s.companyId=:cid")
    List<Map<String, Object>> findByPaymentIdAndCpmpanyId(@Param("id") BigInteger id,
                                                          @Param("cid") BigInteger cid);

    @Query("select p from PaymentDetail p where p.payment.id in :ids and p.companyId=:cid")
    List<PaymentDetail> findAllByPaymentIdsAndCompanyId(@Param("ids") List<BigInteger> ids,
                                                        @Param("cid") BigInteger cid);

    @Query(value = "select  p from PaymentDetail p where p.payment.id=:id and p.companyId=:cid")
    List<PaymentDetail> findAllByPaymentIdAndCompanyId(@Param("id") BigInteger id,BigInteger cid );

    @Modifying
    @Transactional
    @Query("DELETE FROM PaymentDetail c WHERE c.payment.id = :id and c.companyId=:cid")
    void deleteAllByPaymentIdAndCompanyId(@Param("id") BigInteger id,
                                          @Param("cid") BigInteger cid);
}
