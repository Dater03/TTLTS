package com.example.ttlts.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "delivery")
public class Delivery {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    int shippingMethodId;
    int customerId;
    int deliverId;
    int projectId;
    String deliveryAddress;
    LocalDateTime estimateDeliveryTime;
    LocalDateTime actualDeliveryTime;
    @Enumerated(EnumType.STRING)
    DeliveryStatus deliveryStatus;

    @ManyToOne
    @JoinColumn(name = "shippingMethodId",insertable = false, updatable = false,nullable = false)
    @JsonIgnore
    ShippingMethod shippingmethod;

    @ManyToOne
    @JoinColumn(name = "projectId",insertable = false, updatable = false,nullable = false)
    @JsonIgnore
    Project project;

    @ManyToOne
    @JoinColumn(name = "customerId",insertable = false, updatable = false)
    @JsonBackReference
    Customer customer;
}

