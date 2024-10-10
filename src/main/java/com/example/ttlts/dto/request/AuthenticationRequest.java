package com.example.ttlts.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
// chi dung 2 cai, 1 la username+password, hoac email+password
public class AuthenticationRequest {
    String username;
    String password;
}
