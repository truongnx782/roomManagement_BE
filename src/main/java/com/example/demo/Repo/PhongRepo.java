package com.example.demo.Repo;

import com.example.demo.Entity.Phong;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigInteger;

public interface PhongRepo extends JpaRepository<Phong, BigInteger> {
}
