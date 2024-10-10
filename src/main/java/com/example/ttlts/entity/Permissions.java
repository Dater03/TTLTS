package com.example.ttlts.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "permissions")
public class Permissions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    int userId;
    int roleId;

    @ManyToOne
    @JoinColumn(name = "userId",insertable = false, updatable = false, nullable = false)
    @JsonBackReference
    User user;

    @ManyToOne
    @JoinColumn(name = "roleId",insertable = false, updatable = false, nullable = false)
    @JsonBackReference
    Role role;
}
