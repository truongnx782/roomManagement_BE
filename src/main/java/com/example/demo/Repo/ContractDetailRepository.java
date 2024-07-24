package com.example.demo.Repo;

import com.example.demo.DTO.ContractDetailDTO;
import com.example.demo.Entity.Contract;
import com.example.demo.Entity.ContractDetail;
import com.example.demo.Entity.Customer;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigInteger;
import java.util.List;

public interface ContractDetailRepository extends JpaRepository<ContractDetail, BigInteger> {
    @Query(value = "select c from ContractDetail  c where c.contract.id=:id")
    List<ContractDetail> findByContractId(@Param("id") BigInteger id);

    @Query(value = "select c.customer from ContractDetail c where c.contract.id=:id ")
    List<Customer> findAllCustomerById(@Param("id") BigInteger id);

    @Modifying
    @Transactional
    @Query("DELETE FROM ContractDetail c WHERE c.contract.id = :id")
    void deleteAllByContactId(@Param("id") BigInteger id);
}
