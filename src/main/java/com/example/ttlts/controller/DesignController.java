package com.example.ttlts.controller;

import com.example.ttlts.entity.Design;
import com.example.ttlts.entity.Project;
import com.example.ttlts.service.Service.DesignService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RestController
@RequestMapping("/design")
public class DesignController {
    DesignService designService;

    @PostMapping
    @PreAuthorize("hasRole('Designer')")
    public ResponseEntity<Design> createDesign(@RequestBody Design design) {
        Design createdDesign = designService.createDesign(design);
        return ResponseEntity.ok(createdDesign);
    }

    @PutMapping("/project/{projectId}/approve/{id}")
    public ResponseEntity<Void> approveDesign(@PathVariable int projectId,@PathVariable int id) {
        designService.approveDesign(projectId,id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/project/{projectId}/reject/{id}")
    @PreAuthorize("hasRole('Leader')")
    public ResponseEntity<Void> rejectDesign(@PathVariable int id) {
        designService.rejectDesign(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/project/{projectId}")
    public ResponseEntity<List<Design>> getDesignsByProjectId(@PathVariable int projectId) {
        List<Design> designs = designService.getDesignsByProjectId(projectId);
        return ResponseEntity.ok(designs);
    }

    @GetMapping("/{projectId}/{designId}")
    public Project getProject(@PathVariable int projectId,@PathVariable int designId) {
        return designService.getProjectByDesign(projectId,designId);
    }

}
