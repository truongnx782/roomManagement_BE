package com.example.demo.repository;

import com.example.demo.entity.ContractDetail;
import com.example.demo.entity.Customer;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.List;

public interface ContractDetailRepository extends JpaRepository<ContractDetail, Long> {
    @Query(value = "select c from ContractDetail  c where c.contract.id=:id and c.companyId=:cid")
    List<ContractDetail> findByContractIdAndCompanyId( @Param("id") Long id,@Param("cid") Long cid);

    @Query(value = "select c.customer from ContractDetail c where c.contract.id=:id and c.companyId=:cid")
    List<Customer> findAllCustomerByContractIdAndCompanyId(@Param("id") Long id,
                                                           @Param("cid")Long cid);

    @Modifying
    @Transactional
    @Query("DELETE FROM ContractDetail c WHERE c.contract.id = :id AND c.companyId=:cid")
    void deleteAllByContactIdAndCompanyId(@Param("id") Long id,
                                          @Param("cid") Long cid);

    List<ContractDetail> findByContractIdInAndCompanyId(List<Long> contractIds, Long cid);
}
