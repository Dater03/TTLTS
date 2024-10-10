package com.example.ttlts.repository;

import com.example.ttlts.entity.ConfirmEmail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConfirmEmailRepository extends JpaRepository<ConfirmEmail, Integer> {
    Optional<ConfirmEmail> findByUserIdAndConfirmCode(int userId, String confirmCode);
}
