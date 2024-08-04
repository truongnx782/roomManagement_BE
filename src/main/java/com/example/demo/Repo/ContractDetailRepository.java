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
    @Query(value = "select c from ContractDetail  c where c.contract.id=:id and c.companyId=:cid")
    List<ContractDetail> findByContractIdAndCompanyId( @Param("id") BigInteger id,@Param("cid") BigInteger cid);

    @Query(value = "select c.customer from ContractDetail c where c.contract.id=:id and c.companyId=:cid")
    List<Customer> findAllCustomerByContractIdAndCompanyId(@Param("id") BigInteger id,
                                                           @Param("cid")BigInteger cid);

    @Modifying
    @Transactional
    @Query("DELETE FROM ContractDetail c WHERE c.contract.id = :id AND c.companyId=:cid")
    void deleteAllByContactIdAndCompanyId(@Param("id") BigInteger id,
                                          @Param("cid") BigInteger cid);
}
