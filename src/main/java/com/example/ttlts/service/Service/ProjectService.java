package com.example.ttlts.service.Service;

import com.example.ttlts.entity.*;
import com.example.ttlts.repository.CustomerRepository;
import com.example.ttlts.repository.ProjectRepository;
import com.example.ttlts.repository.ResourceRepository;
import com.example.ttlts.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class ProjectService {
    ProjectRepository projectRepository;
    UserRepository userRepository;
    ResourceRepository resourceRepository;
    EmailService emailService;
    CustomerRepository customerRepository;

    public Project createProject(Project project) {
        project.setStartDate(LocalDateTime.now());
        project.setExpectedEndDate(project.getExpectedEndDate() != null ? project.getExpectedEndDate() : LocalDateTime.now().plusMonths(3)); // default 3 months
        project.setProjectStatus(ProjectStatus.DESIGNING);
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


    @Transactional
    public void confirmPrinting(int projectId, List<Integer> resourceIds) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found with ID: " + projectId));

        // Check if project status is DESIGN_APPROVED
        if (project.getProjectStatus() == ProjectStatus.DESIGN_APPROVED) {
            for (int resourceId : resourceIds) {
                Resource resource = resourceRepository.findById(resourceId)
                        .orElseThrow(() -> new RuntimeException("Resource not found with ID: " + resourceId));

                if (resource.getResourceType() == ResourceType.CONSUMABLE) {
                    resource.consume(1); // Adjust the quantity based on requirement
                }

                resourceRepository.save(resource); // Save updated resource
            }
            project.setProjectStatus(ProjectStatus.PRINTING_CONFIRMED); // Update project status
            projectRepository.save(project); // Save updated project status
        } else {
            throw new IllegalStateException("Project is not in the correct status for printing confirmation.");
        }
    }


    public void completePrinting(int projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        project.setProjectStatus(ProjectStatus.PRINTED);
        projectRepository.save(project);

        Customer customer = customerRepository.findById(project.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        // Send email to customer
        emailService.sendEmail(customer.getEmail(), "Project Print Completed", "Your project is ready!");
    }

    public List<Project> getCompletedPrintingProjects() {
        return projectRepository.findByProjectStatusIs(ProjectStatus.PRINTING);
    }
}