package com.example.ttlts.service.Service;

import com.example.ttlts.entity.*;
import com.example.ttlts.repository.DeliveryRepository;
import com.example.ttlts.repository.ProjectRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class DeliveryService {
    DeliveryRepository deliveryRepository;
    ProjectRepository projectRepository;
    EmailService emailService;
    NotificationService notificationService;

    public void assignDelivery(int projectId, int shipperId, int shippingId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        Delivery delivery = new Delivery();
        delivery.setProjectId(projectId);
        delivery.setDeliverId(shipperId);
        delivery.setCustomerId(project.getCustomerId());
        delivery.setShippingMethodId(shippingId);
        delivery.setDeliveryStatus(DeliveryStatus.IN_PROGRESS);

        deliveryRepository.save(delivery);

        project.setProjectStatus(ProjectStatus.DELIVERING);
        projectRepository.save(project);
    }


    public void confirmDelivery(int deliveryId) {
        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new RuntimeException("Delivery not found"));

        delivery.setDeliveryStatus(DeliveryStatus.DELIVERED);
        Project project = delivery.getProject();
        project.setProjectStatus(ProjectStatus.DELIVERED);
        deliveryRepository.save(delivery);
        projectRepository.save(project);

        // Notify customer and leaders
        Customer customer = delivery.getCustomer();
        emailService.sendEmail(customer.getEmail(), "Delivery Confirmed", "Delivery Confirmed");
        notificationService.sendNotification(project.getUser(), "Delivery for project " + project.getId() + " has been completed.", "/project/" + project.getId());
    }
}
