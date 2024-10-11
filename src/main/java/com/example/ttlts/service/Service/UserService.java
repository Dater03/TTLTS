package com.example.ttlts.service.Service;

import com.example.ttlts.entity.User;
import com.example.ttlts.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class UserService {
    UserRepository userRepository;
    PasswordEncoder passwordEncoder;
    EmailService emailService;
    private final AuthService authService;

    // Đăng ký người dùng mới
    public User register(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());

        User savedUser = userRepository.save(user);

        // Gửi email xác nhận
        if (savedUser.getEmail() != null && !savedUser.getEmail().isEmpty()) {
            String subject = "Chao mung den voi dich vu cua chung toi!";
            String text = "Dear " + savedUser.getFullName() + ", your account has been created successfully.";
            emailService.sendEmail(savedUser.getEmail(), subject, text);
        }
        return savedUser;
    }

    // Đăng nhập và tạo JWT
    public String login(String username, String password) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BadCredentialsException("Invalid password");
        }

        return authService.createToken(user);
    }

    // Quên mật khẩu - Gửi mã xác nhận qua email
    public void forgotPassword(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        String subject = "Dear, "+user.getFullName()+", your account has been reset token successfully.";
        String resetToken = UUID.randomUUID().toString();
        emailService.sendResetToken(email, resetToken);
        emailService.sendEmail(email, subject , resetToken);
    }

    // Xác nhận tạo mật khẩu mới qua token
    // chua chay
    public void confirmPassword(String token, String newPassword) {
        try {
            User user = validateToken(token);

            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);
        } catch (UsernameNotFoundException e) {
            throw new RuntimeException("Invalid token: User not found for the provided token", e);
        } catch (RuntimeException e) {
            throw new RuntimeException("Invalid token", e);
        }
    }

    // Đổi mật khẩu
    public void changePassword(String username, String oldPassword, String newPassword) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new BadCredentialsException("Invalid old password");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    private User validateToken(String token) {
        String username;
        try {
            username = authService.decodeToken(token);
        } catch (JwtException e) {
            throw new RuntimeException("Invalid token");
        }

        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found for the provided token"));
    }

}