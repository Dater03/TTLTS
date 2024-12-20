package com.example.ttlts.repository;

import com.example.ttlts.entity.Permissions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PermissionsRepository extends JpaRepository<Permissions, Integer> {
    Optional<Permissions> findByUserIdAndRoleId(int userId, int roleId);
    void deleteByUserId(int userId);
}
