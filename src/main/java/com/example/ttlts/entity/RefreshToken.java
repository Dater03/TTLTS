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
@Table(name = "refeshtoken")
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    int userId;
    String token;
    LocalDateTime expiryTime;
    LocalDateTime createTime;

    @ManyToOne
    @JoinColumn(name = "userId",insertable = false, updatable = false, nullable = false)
    @JsonBackReference
    User user;
}
