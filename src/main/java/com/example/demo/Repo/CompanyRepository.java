package com.example.demo.Repo;

import com.example.demo.Entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<Company, BigInteger> {
    Optional<Company> findByCompanyCode(String admin);
}
