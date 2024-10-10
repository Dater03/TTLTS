package com.example.ttlts.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {
    int id;
    String username;
    String password;
    String fullName;
    LocalDateTime dateOfBirth;
    String avatar;
    String email;
    LocalDateTime  createTime;
    LocalDateTime  updateTime;
    String phoneNumber;
    int teamId;
    Boolean isActive;
}
