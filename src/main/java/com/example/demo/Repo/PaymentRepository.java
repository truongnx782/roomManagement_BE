package com.example.demo.Repo;

import com.example.demo.Entity.Contract;
import com.example.demo.Entity.Payment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, BigInteger> {
    @Query(value = "SELECT u FROM Payment u left join ContractDetail cd on u.contract.id=cd.contract.id WHERE " +
            "( u.contract.room.roomName LIKE %:search% OR" +
            " u.contract.room.roomCode LIKE %:search% or" +
            " cd.customer.phoneNumber LIKE %:search% or " +
            " cd.customer.customerName LIKE %:search% ) " +
            "AND (:paymentStatus IS NULL OR u.paymentStatus = :paymentStatus)  AND u.status=1" +
            "ORDER BY u.id DESC")
    Page<Payment> search(@Param("search") String search,
                          @Param("paymentStatus") Integer paymentStatus,
                          Pageable pageable);

    @Query(value = "select  p from Payment  p where p.contract.id=:id")
    List<Payment> findAllByContractId(@Param("id") BigInteger id);

    @Query("SELECT p FROM Payment p WHERE p.paymentDate = (SELECT MAX(p2.paymentDate) FROM Payment p2 WHERE p2.contract.id = p.contract.id AND p2.status = 1) AND p.status = 1 order by p.id asc ")
    List<Payment> findPaymentsWithMaxDatePerContract();
}
