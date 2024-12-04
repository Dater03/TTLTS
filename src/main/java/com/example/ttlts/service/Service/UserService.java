package com.example.ttlts.service.Service;

import com.example.ttlts.dto.request.ChangePasswordRequest;
import com.example.ttlts.dto.request.RegisterRequest;
import com.example.ttlts.dto.request.ResetPasswordRequest;
import com.example.ttlts.dto.response.UserResponse;
import com.example.ttlts.entity.*;
import com.example.ttlts.exception.AppException;
import com.example.ttlts.exception.ErrException;
import com.example.ttlts.repository.*;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class UserService {
    UserRepository userRepository;
    PasswordEncoder passwordEncoder;
    EmailService emailService;
    RefeshTokenRepository refeshTokenRepository;
    AuthService authService;
    RefreshTokenService refreshTokenService;
    ConfirmEmailRepository confirmEmailRepository;
    TeamRepository teamRepository;
    PermissionsRepository permissionsRepository;
    RoleRepository roleRepository;

    private static final Map<Integer, String> TEAM_ROLE_MAPPING = Map.of(
            1, "Admin",
            2, "Employee",
            3, "Designer",
            4, "Deliver",
            5, "Leader",
            6, "Manager"
    );

    // Đăng ký người dùng mới
    public boolean register(RegisterRequest registerRequest) {
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            return false;
        }
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setFullName(registerRequest.getFullName());
        user.setEmail(registerRequest.getEmail());
        user.setPhoneNumber(registerRequest.getPhoneNumber());
        user.setTeamId(registerRequest.getTeamId());
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());

        User savedUser = userRepository.save(user);

        Team team = teamRepository.findById(savedUser.getTeamId())
                .orElseThrow(() -> new RuntimeException("Team not found"));
        team.setNumberOfMember(team.getNumberOfMember() + 1);
        teamRepository.save(team);

        Role chosenRole = roleRepository.findByRoleName(registerRequest.getRole())
                .orElseThrow(() -> new RuntimeException("Role '" + registerRequest.getRole() + "' not found"));

        Permissions permissions = Permissions.builder()
                .userId(savedUser.getId())
                .roleId(chosenRole.getId())
                .build();
        permissionsRepository.save(permissions);

        String subject = "Welcome to our service!";
        String text = "Dear " + savedUser.getFullName() + ", your account has been created successfully.";
        emailService.sendEmail(savedUser.getEmail(), subject, text);

        return true;
    }

    // Đăng nhập và tạo JWT
    // chuyen sang authService
    public String login(String username, String password) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BadCredentialsException("Invalid password");
        }

        String token = authService.createToken(user);
        refreshTokenService.saveToken(user, token);

        return token;
    }

    // Quên mật khẩu - Gửi mã xác nhận qua email
    public void sendVerificationCode(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User with email does not exist"));

        String code = UUID.randomUUID().toString();
        ConfirmEmail confirmEmail = ConfirmEmail.builder()
                .userId(user.getId())
                .confirmCode(code)
                .expiryTime(LocalDateTime.now().plusMinutes(30))
                .isConfirm(false)
                .build();

        confirmEmailRepository.save(confirmEmail);

        emailService.sendResetToken(email,code);
        emailService.sendEmail(email, "Password Reset Verification", "Your verification code is: " + code);
    }

    // Xác nhận tạo mật khẩu mới qua token
//    public void confirmPassword(String token, String newPassword) {
//        try {
//            User user = authService.decodeToken(token); // Validate token bằng decodeToken
//            user.setPassword(passwordEncoder.encode(newPassword)); // Đặt mật khẩu mới
//            userRepository.save(user);
//            refeshTokenRepository.deleteByToken(token); // Xóa token sau khi xác nhận
//        } catch (Exception e) {
//            throw new BadCredentialsException("Invalid token");
//        }
//    }
    public boolean resetPassword(ResetPasswordRequest request) {
        ConfirmEmail confirmEmail = confirmEmailRepository.findByUserIdAndConfirmCode(
                request.getUserId(), request.getConfirmCode());

        if (confirmEmail == null || confirmEmail.getExpiryTime().isBefore(LocalDateTime.now()) || confirmEmail.getIsConfirm()) {
            return false;
        }

        User user = userRepository.findById(request.getUserId());
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        confirmEmail.setIsConfirm(true);
        confirmEmailRepository.save(confirmEmail);

        return true;
    }


    // Đổi mật khẩu
    public boolean changePassword(ChangePasswordRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (user != null && passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            user.setPassword(passwordEncoder.encode(request.getNewPassword()));
            userRepository.save(user);
            return true;
        }
        return false;
    }

    public void logout(String token) {
        refeshTokenRepository.deleteByToken(token);
    }

    public List<User> getAllUser(){
        return userRepository.findAll();
    }

    // Chuyển phòng ban cho nhân viên
    @Transactional
    public User changeUserTeam(int userId, int oldTeamId, int newTeamId) {
        User user = userRepository.findById(userId);

        if (oldTeamId == newTeamId) {
            throw new RuntimeException("New team must be different from the old team");
        }

        if (user.getTeamId() == oldTeamId){
            user.setTeamId(newTeamId);
            user.setUpdateTime(LocalDateTime.now());

            Team teamOld = teamRepository.findById(oldTeamId)
                    .orElseThrow(() -> new RuntimeException("Old team not found"));
            teamOld.setNumberOfMember(teamOld.getNumberOfMember() - 1);

            Team teamNew = teamRepository.findById(newTeamId)
                    .orElseThrow(() -> new RuntimeException("New team not found"));
            teamNew.setNumberOfMember(teamNew.getNumberOfMember() + 1);

            // Lấy role dựa trên teamId từ ánh xạ
            String newRoleName = TEAM_ROLE_MAPPING.get(newTeamId);
            if (newRoleName != null) {
                Role newRole = roleRepository.findByRoleName(newRoleName)
                        .orElseThrow(() -> new RuntimeException("Role '" + newRoleName + "' not found"));

                // Xóa role cũ của nhân viên và gán role mới
                permissionsRepository.deleteByUserId(user.getId());
                Permissions newPermissions = Permissions.builder()
                        .userId(user.getId())
                        .roleId(newRole.getId())
                        .build();
                permissionsRepository.save(newPermissions);
            } else {
                throw new RuntimeException("No role mapping found for the new team");
            }


            teamRepository.save(teamOld);
            teamRepository.save(teamNew);

            return userRepository.save(user);
        } else {
            throw new RuntimeException("Old team id mismatch");
        }
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(int id) {
        return userRepository.findById(id);
    }

    private boolean isPhoto(MultipartFile file) {
        String contentType = file.getContentType();
        return contentType != null && contentType.startsWith("image/");
    }
    private String storeFile(MultipartFile file) {
        if (!isPhoto(file) || file.getOriginalFilename() == null || file.getOriginalFilename().isEmpty()) {
            throw new AppException(ErrException.NOT_FILE);
        }
        String filename = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        String uniqueFilename = UUID.randomUUID().toString() +"_" + filename;
        java.nio.file.Path uploadDir = Paths.get("upload");
        if (!Files.exists(uploadDir)) {
            try {
                Files.createDirectories(uploadDir);
            } catch (IOException e) {
                throw new AppException((ErrException.DIRECTORY_CREATION_FAILD));
            }
        }
        java.nio.file.Path destination = Paths.get(uploadDir.toString(), uniqueFilename);
        try {
            Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new AppException((ErrException.FILE_STORAGE_FAILD));
        }
        return uniqueFilename;
    }
    public User uploadUserPhoto(int userId, MultipartFile file) {
        User user = userRepository.findById(userId);
        String filename = storeFile(file);
        user.setAvatar(filename);
        return userRepository.save(user);
    }
}
