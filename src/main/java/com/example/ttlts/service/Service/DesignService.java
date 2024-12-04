package com.example.ttlts.service.Service;

import com.example.ttlts.entity.*;
import com.example.ttlts.repository.DesignRepository;
import com.example.ttlts.repository.ProjectRepository;
import com.example.ttlts.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DesignService {
    DesignRepository designRepository;
    ProjectRepository projectRepository;
    UserRepository userRepository;
    NotificationService notificationService;

    public Design createDesign(Design design) {
        design.setDesignStatus(DesignStatus.PENDING);
        design.setDesignTime(LocalDateTime.now());
        return designRepository.save(design);
    }

    public void approveDesign(int projectId,int designId) {
        Design design = designRepository.findById(designId)
                .orElseThrow(() -> new RuntimeException("Design not found"));
        Project project = design.getProject();
        User user = design.getUser();

        design.setDesignStatus(DesignStatus.APPROVED);
        design.setApproverId(user.getId());
        project.setProjectStatus(ProjectStatus.DESIGN_APPROVED);

        designRepository.save(design);
        projectRepository.save(project);

        notificationService.sendNotification(user, "Your design has been approved.", "/project/" + project.getId());
    }

    public void rejectDesign(int designId) {
        Design design = designRepository.findById(designId)
                .orElseThrow(() -> new RuntimeException("Design not found"));
        Project project = design.getProject();
        User user = design.getUser();

        design.setDesignStatus(DesignStatus.REJECTED);
        project.setProjectStatus(ProjectStatus.DESIGN_REJECTED);
        designRepository.save(design);

        // Notify designer
        notificationService.sendNotification(user, "Your design has been rejected.", "/project/" + project.getId());
    }

    public List<Design> getDesignsByProjectId(int id) {
        List<Design> design = designRepository.findByProjectId(id).stream().toList();
        return design;
    }

    public Project getProjectByDesign(int projectId, int designId) {
        return designRepository.findProjectByProjectIdAndDesignId(projectId,designId).orElseThrow(() -> new RuntimeException("Project not found"));
    }
}