package com.example.demo.Repo;

import com.example.demo.Entity.DichVu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigInteger;

public interface DichVuRepo extends JpaRepository<DichVu, BigInteger> {
}
