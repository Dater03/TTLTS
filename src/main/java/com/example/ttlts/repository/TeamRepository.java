package com.example.ttlts.repository;

import com.example.ttlts.entity.Team;
import com.fasterxml.classmate.TypeBindings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TeamRepository extends JpaRepository<Team, Integer> {
    Optional<Team> findByName(String name);
}
