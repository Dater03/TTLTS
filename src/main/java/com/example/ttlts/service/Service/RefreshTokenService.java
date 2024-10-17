package com.example.ttlts.service.Service;

import com.example.ttlts.entity.RefreshToken;
import com.example.ttlts.entity.User;
import com.example.ttlts.repository.RefeshTokenRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class RefreshTokenService {
    RefeshTokenRepository refreshTokenRepository;

    // Lưu token vào bảng RefreshToken
    public void saveToken(User user, String token) {
        RefreshToken refreshToken = RefreshToken.builder()
                .userId(user.getId())
                .token(token)
                .expiryTime(LocalDateTime.now().plusHours(1))  // Token hết hạn sau 1 giờ
                .createTime(LocalDateTime.now())
                .build();
        refreshTokenRepository.save(refreshToken);
    }

    public RefreshToken generateNewToken(User user) {
        String token = UUID.randomUUID().toString();
        return refreshTokenRepository.save(
                RefreshToken.builder()
                        .userId(user.getId())
                        .token(token)
                        .expiryTime(LocalDateTime.now().plusDays(7))
                        .user(user)
                        .build()
        );
    }
}
