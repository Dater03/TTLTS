package com.example.ttlts.repository;

import com.example.ttlts.entity.Design;
import com.example.ttlts.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DesignRepository extends JpaRepository<Design, Integer> {
    List<Design> findByProjectId(int projectId);
    // In ProjectRepository.java
    @Query("SELECT p FROM Project p JOIN p.design d WHERE p.id = :projectId AND d.id = :designId")
    Optional<Project> findProjectByProjectIdAndDesignId(@Param("projectId") int projectId, @Param("designId") int designId);
}
