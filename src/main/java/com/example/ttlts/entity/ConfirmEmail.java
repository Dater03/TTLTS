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
@Table(name = "confirmemail")
public class ConfirmEmail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    int userId;
    String confirmCode;
    LocalDateTime expiryTime;
    LocalDateTime createTime;
    Boolean isConfirm;

    @ManyToOne
    @JoinColumn(name = "userId",insertable = false, updatable = false, nullable = false)
    @JsonBackReference
    User user;
}
