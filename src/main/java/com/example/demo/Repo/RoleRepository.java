package com.example.demo.Repo;

import com.example.demo.Entity.Role;
import com.example.demo.Util.ERole;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(ERole name);
}
