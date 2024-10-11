package com.example.ttlts.service.Service;

import com.example.ttlts.entity.RefreshToken;
import com.example.ttlts.entity.User;
import com.example.ttlts.repository.RefeshTokenRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

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

    // Xóa token sau khi đã sử dụng
    public void deleteByToken(String token) {
        refreshTokenRepository.deleteByToken(token);
    }

    // Kiểm tra token có tồn tại và hợp lệ
    public boolean isValidToken(String token) {
        return refreshTokenRepository.findByToken(token)
                .map(refreshToken -> refreshToken.getExpiryTime().isAfter(LocalDateTime.now()))
                .orElse(false);
    }
}
