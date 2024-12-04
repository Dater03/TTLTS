package com.example.ttlts.service.Service;

import com.example.ttlts.entity.Notification;
import com.example.ttlts.entity.User;
import com.example.ttlts.repository.NotificationRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class NotificationService {
    NotificationRepository notificationRepository;
    EmailService emailService;

    public void sendNotification(User user, String content, String link) {
        Notification notification = Notification.builder()
                .userId(user.getId())
                .content(content)
                .link(link)
                .createTime(LocalDateTime.now())
                .isSend(false)
                .build();
        notificationRepository.save(notification);

        emailService.sendEmail(user.getEmail(), "Notification", content);
    }
}