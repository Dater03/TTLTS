package com.example.ttlts.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
@Table(name = "customerfeedback")
public class CustomerFeedback {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    int projectId;
    int customerId;
    String feedbackContent;
    String responseByCompany;
    int userFeedbackId;
    LocalDateTime feedbackTime;
    LocalDateTime responseTime;;

    @ManyToOne
    @JoinColumn(name = "projectId",insertable = false, updatable = false,nullable = false)
    @JsonBackReference
    Project project;

    @ManyToOne
    @JoinColumn(name = "customerId",insertable = false, updatable = false,nullable = false)
    @JsonBackReference
    Customer customer;

    @ManyToOne
    @JoinColumn(name = "userFeedbackId",insertable = false, updatable = false,nullable = false)
    @JsonBackReference
    User user;

}
