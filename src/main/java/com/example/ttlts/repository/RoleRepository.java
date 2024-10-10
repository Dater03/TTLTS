package com.example.ttlts.repository;

import com.example.ttlts.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findById(int id);

    Optional<Role> findByRoleCode(String name);
    Optional<Role> findByRoleName(String name);
}
