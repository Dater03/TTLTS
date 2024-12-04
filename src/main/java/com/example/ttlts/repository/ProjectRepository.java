package com.example.ttlts.repository;

import com.example.ttlts.entity.Project;
import com.example.ttlts.entity.ProjectStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project,Integer> {
    Optional<Project> findById(int id);
    List<Project> findByProjectStatusIs(ProjectStatus projectStatus);
}
