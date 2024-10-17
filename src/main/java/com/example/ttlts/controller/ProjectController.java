package com.example.ttlts.controller;

import com.example.ttlts.entity.Project;
import com.example.ttlts.entity.User;
import com.example.ttlts.service.Service.ProjectService;
import com.example.ttlts.service.Service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RestController
@RequestMapping("/project")
public class ProjectController {
    UserService userService;
    ProjectService projectService;

    // Tạo Project (Nhân viên có quyền Employee trong phòng ban Sales)
    @PostMapping("/create")
    @PreAuthorize("hasRole('Employee')")
    public ResponseEntity<?> createProject(@RequestBody Project project) {
        Project createdProject = projectService.createProject(project);
        return new ResponseEntity<>(createdProject, HttpStatus.CREATED);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Project>> getAllProjects() {
        List<Project> projects = projectService.getAllProjects();
        return new ResponseEntity<>(projects, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProjectById(@PathVariable int id) {
        try {
            Project project = projectService.getProjectById(id);
            return new ResponseEntity<>(project, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
