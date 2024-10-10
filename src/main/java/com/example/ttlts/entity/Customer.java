package com.example.ttlts.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "customer")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    String fullName;
    String phoneNumber;
    String address;

    @OneToMany(mappedBy = "customer")
    @JsonManagedReference
    List<CustomerFeedback> customerfeedback;

    @OneToMany(mappedBy = "customer")
    @JsonManagedReference
    List<Bill> bill;

    @OneToMany(mappedBy = "customer")
    @JsonManagedReference
    List<Project> project;

    @OneToMany(mappedBy = "customer")
    @JsonManagedReference
    List<Delivery> delivery;
}
