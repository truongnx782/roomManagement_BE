package com.example.demo.Repo;

import com.example.demo.Entity.User_Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigInteger;

public interface User_RoleRepository extends JpaRepository<User_Role, BigInteger> {
}
