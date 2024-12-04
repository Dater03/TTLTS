package com.example.ttlts.controller;

import com.example.ttlts.service.Service.DeliveryService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RestController
@RequestMapping("/delivery")
public class DeliveryController {
    DeliveryService deliveryService;

    @PutMapping("/{projectId}/{shipperId}/{shippingId}/assign")
    @PreAuthorize("hasRole('Leader')")
    public ResponseEntity<Void> assignDelivery(@PathVariable int projectId, @PathVariable int shipperId, @PathVariable int shippingId) {
        deliveryService.assignDelivery(projectId, shipperId, shippingId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/confirm")
    @PreAuthorize("hasRole('Deliver')")
    public ResponseEntity<Void> confirmDelivery(@PathVariable int id) {
        deliveryService.confirmDelivery(id);
        return ResponseEntity.ok().build();
    }
}
