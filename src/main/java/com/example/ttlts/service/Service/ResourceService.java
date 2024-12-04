package com.example.ttlts.service.Service;

import com.example.ttlts.entity.Resource;
import com.example.ttlts.repository.ResourceRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class ResourceService {
    ResourceRepository resourceRepository;
    public List<Resource> getAllResources() {
        return resourceRepository.findAll();
    }

    public Optional<Resource> getResourceById(int id) {
        return resourceRepository.findById(id);
    }

    public Resource createResource(Resource resource) {
        return resourceRepository.save(resource);
    }

    public Resource updateResource(int id, Resource resourceDetails) {
        return resourceRepository.findById(id).map(resource -> {
            resource.setResourceName(resourceDetails.getResourceName());
            resource.setImage(resourceDetails.getImage());
            resource.setResourceType(resourceDetails.getResourceType());
            resource.setAvailableQuantity(resourceDetails.getAvailableQuantity());
            resource.setResourceStatus(resourceDetails.getResourceStatus());
            return resourceRepository.save(resource);
        }).orElseThrow(() -> new RuntimeException("Resource not found"));
    }

    public void deleteResource(int id) {
        resourceRepository.deleteById(id);
    }
}