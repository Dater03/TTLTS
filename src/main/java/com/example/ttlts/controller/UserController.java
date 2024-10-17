package com.example.ttlts.controller;

import com.example.ttlts.dto.request.*;
import com.example.ttlts.dto.response.ApiResponse;
import com.example.ttlts.dto.response.AuthenticationResponse;
import com.example.ttlts.entity.User;
import com.example.ttlts.service.Service.AuthService;
import com.example.ttlts.service.Service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@CrossOrigin("*")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RestController
@RequestMapping("/auth")
public class UserController {
    UserService userService;
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody RegisterRequest registerRequest) {
        boolean isRegistered = userService.register(registerRequest);
        if (isRegistered) {
            return ResponseEntity.ok("User registered successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Registration failed. Username or email already exists.");
        }
    }

    @PostMapping("/login")
    public ApiResponse<AuthenticationResponse> createToken(@RequestBody AuthenticationRequest authenticationRequest) {
        return ApiResponse.<AuthenticationResponse>builder()
                .result(authService.authenticationResponse(authenticationRequest))
                .code(200)
                .build();
    }


    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody ForgotPasswordRequest forgotPasswordRequest) {
        userService.sendVerificationCode(forgotPasswordRequest.getEmail());
        return ResponseEntity.ok("Verification code sent to your email.");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordRequest resetPasswordRequest) {
        boolean isReset = userService.resetPassword(resetPasswordRequest);
        if (isReset) {
            return ResponseEntity.ok("Password has been reset successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid verification code or password.");
        }
    }

//    @PostMapping("/change-password")
//    public ResponseEntity<?> changePassword(@RequestBody Map<String, String> request) {
//        userService.changePassword(
//                request.get("username"),
//                request.get("oldPassword"),
//                request.get("newPassword")
//        );
//        return ResponseEntity.ok("Password updated successfully");
//    }

    @PutMapping("/change-password")
    public ResponseEntity<String> changePassword(@RequestBody ChangePasswordRequest changePasswordRequest) {
        boolean isChanged = userService.changePassword(changePasswordRequest);
        if (isChanged) {
            return ResponseEntity.ok("Password changed successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Password change failed.");
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable int id) {
        User user = userService.getUserById(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PutMapping("/photo/{id}")
    public ApiResponse<User> uploadPhoto(@PathVariable("id") int id, @RequestParam("file") MultipartFile file) {
        User user = userService.uploadUserPhoto(id, file);
        return ApiResponse.<User>builder()
                .result(user)
                .code(200)
                .build();
    }
}
