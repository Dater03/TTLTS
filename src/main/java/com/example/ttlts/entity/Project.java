package com.example.ttlts.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "project")
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    String projectName;
    String requestDescriptionFromCustomer;
    LocalDateTime startDate;
    String image;
    int employeeId;
    LocalDateTime expectedEndDate;
    int customerId;
    ProjectStatus projectStatus;

    @OneToMany(mappedBy = "project")
    @JsonManagedReference
    List<CustomerFeedback> customerfeebback;

    @OneToMany(mappedBy = "project")
    @JsonManagedReference
    List<Delivery> delivery;

    @OneToMany(mappedBy = "project")
    @JsonManagedReference
    List<Design> design;

    @ManyToOne
    @JoinColumn(name = "customerId",insertable = false, updatable = false,nullable = false)
    @JsonBackReference
    Customer customer;

    @ManyToOne
    @JoinColumn(name = "employeeId",insertable = false, updatable = false,nullable = false)
    @JsonBackReference
    User user;

}
