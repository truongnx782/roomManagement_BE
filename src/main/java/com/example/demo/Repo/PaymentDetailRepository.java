package com.example.demo.Repo;

import com.example.demo.Entity.PaymentDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

public interface PaymentDetailRepository extends JpaRepository<PaymentDetail, BigInteger> {
//    @Query(value = "select  s.id as id,p.amountToPay/s.servicePrice as value ,p.amountToPay as amount" +
//            " from PaymentDetail p left join Service s on p.service.id=s.id where p.payment.id=:id")
@Query(value = "select  s.id as id,p.amountToPay as value "+
        " from PaymentDetail p left join Service s on p.service.id=s.id where p.payment.id=:id")
    List<Map<String, Object>> findByPaymentId(@Param("id") BigInteger id);


    @Query("select p from PaymentDetail p where p.payment.id in :ids")
    List<PaymentDetail> findAllByPaymentIds(@Param("ids") List<BigInteger> ids);

    @Query(value = "select  p from PaymentDetail p where p.payment.id=:id")
    List<PaymentDetail> findAllByPaymentId(@Param("id") BigInteger id);
}
