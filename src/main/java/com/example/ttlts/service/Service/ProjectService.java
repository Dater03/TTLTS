package com.example.ttlts.service.Service;

import com.example.ttlts.entity.Project;
import com.example.ttlts.entity.User;
import com.example.ttlts.repository.ProjectRepository;
import com.example.ttlts.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class ProjectService {
    ProjectRepository projectRepository;
    UserRepository userRepository;

    public Project createProject(Project project) {
        project.setStartDate(LocalDateTime.now());
        project.setExpectedEndDate(project.getExpectedEndDate() != null ? project.getExpectedEndDate() : LocalDateTime.now().plusMonths(3)); // default 3 months
        return projectRepository.save(project);
    }

    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    public Project getProjectById(int id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found with ID: " + id));
    }

    public boolean isUserInSalesTeam(User user) {
        return "Sales".equalsIgnoreCase(user.getTeam().getName());
    }

    public User getCurrentUser(Authentication authentication) {
        return userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found: " + authentication.getName()));
    }
}