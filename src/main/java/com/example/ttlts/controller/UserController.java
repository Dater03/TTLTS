package com.example.ttlts.controller;

import com.example.ttlts.dto.request.UserRequest;
import com.example.ttlts.dto.response.ApiResponse;
import com.example.ttlts.dto.response.UserResponse;
import com.example.ttlts.entity.User;
import com.example.ttlts.service.UserService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@CrossOrigin("*")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RestController
@RequestMapping("/auth")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping(value = "/register" , consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> register(@RequestBody User user) {
        return ResponseEntity.ok(userService.register(user));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        String token = userService.login(credentials.get("username"), credentials.get("password"));
        return ResponseEntity.ok(Collections.singletonMap("token", token));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody Map<String, String> request) {
        userService.forgotPassword(request.get("email"));
        return ResponseEntity.ok("Reset token sent to email");
    }

    @PostMapping("/confirm-password")
    public ResponseEntity<?> confirmPassword(@RequestBody Map<String, String> request) {
        try {
            userService.confirmPassword(request.get("token"), request.get("newPassword"));
            return ResponseEntity.ok("Password changed successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }



    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody Map<String, String> request) {
        userService.changePassword(
                request.get("username"),
                request.get("oldPassword"),
                request.get("newPassword")
        );
        return ResponseEntity.ok("Password updated successfully");
    }
}
