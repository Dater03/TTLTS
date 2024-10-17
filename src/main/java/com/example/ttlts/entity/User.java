package com.example.ttlts.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    String username;
    String password;
    String fullName;
    LocalDateTime  dateOfBirth;
    String avatar;
    String email;
    LocalDateTime  createTime;
    LocalDateTime  updateTime;
    String phoneNumber;
    int teamId;
    Boolean isActive;

    @OneToMany(mappedBy = "user")
    @JsonManagedReference
            @JsonIgnore
    List<ConfirmEmail> confirmemail;

    @OneToMany(mappedBy = "user")
    @JsonManagedReference
    @JsonIgnore
    List<Permissions> permissions;

    @OneToMany(mappedBy = "user")
    @JsonManagedReference
    @JsonIgnore
    List<Notification> notification;

    @OneToMany(mappedBy = "user")
    @JsonManagedReference
    @JsonIgnore
    List<RefreshToken> refreshtoken;

    @OneToMany(mappedBy = "user")
    @JsonManagedReference
    @JsonIgnore
    List<KeyPerformanceIndicators> keyperformanceindicators;

    @OneToMany(mappedBy = "user")
    @JsonManagedReference
    @JsonIgnore
    List<CustomerFeedback> customerfeedbacks;

    @OneToMany(mappedBy = "user")
    @JsonManagedReference
    @JsonIgnore
    List<Bill> bill;

    @OneToMany(mappedBy = "user")
    @JsonManagedReference
    @JsonIgnore
    List<Project> project;

    @OneToMany(mappedBy = "user")
    @JsonManagedReference
    @JsonIgnore
    List<Design> design;

    @OneToMany(mappedBy = "user")
    @JsonManagedReference
    @JsonIgnore
    List<ImportCoupon> importcoupon;

    @ManyToOne
    @JoinColumn(name = "teamId",insertable=false, updatable=false, nullable = false)
    @JsonBackReference
    Team team;
}
