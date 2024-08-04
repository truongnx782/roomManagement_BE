package com.example.demo.Repo;

import com.example.demo.Entity.Payment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, BigInteger> {
    @Query(value = "SELECT u FROM Payment u left join ContractDetail cd on u.contract.id=cd.contract.id WHERE " +
            "( u.contract.room.roomName LIKE %:search% OR" +
            " u.contract.room.roomCode LIKE %:search% or" +
            " cd.customer.phoneNumber LIKE %:search% or " +
            " cd.customer.customerName LIKE %:search% ) " +
            "AND (:paymentStatus IS NULL OR u.paymentStatus = :paymentStatus)  " +
            "AND u.status=1 and u.companyId=:cid AND cd.companyId=:cid " +
            "ORDER BY u.id DESC")
    Page<Payment> search(@Param("search") String search,
                         @Param("paymentStatus") Integer paymentStatus,
                         @Param("cid") BigInteger cid, Pageable pageable);

    @Query(value = "select  p from Payment  p where p.contract.id=:id and p.companyId=:cid")
    List<Payment> findAllByContractIdAndCompanyId(@Param("id") BigInteger id,@Param("cid")BigInteger cid);

    @Query("SELECT p FROM Payment p WHERE p.paymentDate = (SELECT MAX(p2.paymentDate) FROM Payment p2 " +
            "WHERE p2.contract.id = p.contract.id AND p2.status = 1 AND p2.companyId=:cid)" +
            "AND p.status = 1 AND p.companyId=:cid order by p.id asc ")
    List<Payment> findPaymentsWithMaxDatePerContractByCpmpanyId(@Param("cid") BigInteger cid);

    Optional<Payment> findByIdAndCompanyId(BigInteger paymentId, BigInteger cid);
}
