package com.journalintime.service;

import com.journalintime.model.Notification;
import com.journalintime.model.User;
import com.journalintime.repository.NotificationRepository;
import com.journalintime.repository.UserRepository;
import com.journalintime.persistence.hibernate.NotificationHibernateRepository;
import com.journalintime.persistence.hibernate.UserHibernateRepository;
import com.journalintime.dto.NotificationDTO;
import com.journalintime.mapper.Mapper;

import java.util.List;

public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    public NotificationService() {
        this.notificationRepository = new NotificationHibernateRepository();
        this.userRepository = new UserHibernateRepository();
    }

    public void createNotification(Long userId, String message) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Notification notification = Notification.builder()
                .user(user)
                .message(message)
                .read(false)
                .build();

        notificationRepository.save(notification);
    }

    public List<NotificationDTO> getUnreadNotifications(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return notificationRepository.findUnreadByUser(user).stream()
                .map(Mapper::toDTO)
                .collect(java.util.stream.Collectors.toList());
    }

    public long getUnreadCount(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return notificationRepository.findUnreadByUser(user).size();
    }

    public void markAsRead(Long notificationId) {
        notificationRepository.findById(notificationId).ifPresent(n -> {
            n.setRead(true);
            notificationRepository.update(n);
        });
    }

    public void checkAndCreateReminders(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        com.journalintime.persistence.hibernate.NoteHibernateRepository noteRepo = new com.journalintime.persistence.hibernate.NoteHibernateRepository();
        boolean hasNoteToday = noteRepo.findByUser(user).stream()
                .anyMatch(n -> n.getCreatedAt() != null
                        && n.getCreatedAt().toLocalDate().equals(java.time.LocalDate.now()));

        if (!hasNoteToday) {
            com.journalintime.model.enums.AppLanguage lang = user.getLanguage();
            java.util.List<String> messages = (lang == com.journalintime.model.enums.AppLanguage.FR) ? List.of(
                    "N'oubliez pas d'immortaliser vos pensées aujourd'hui, " + user.getUsername() + " !",
                    "Un petit mot pour votre futur moi ? Écrivez une note !",
                    "Chaque pensée compte. Quel est votre ressenti actuel ?",
                    "Une pensée positive pour finir la journée ? Notez-la !",
                    "Votre journal vous attend pour une nouvelle aventure d'introspection.")
                    : List.of(
                            "Don't forget to record your thoughts today, " + user.getUsername() + "!",
                            "A little note for your future self? Write it down!",
                            "Every thought counts. How are you feeling right now?",
                            "One positive thought to end the day? Journal it!",
                            "Your journal is waiting for another reflective adventure.");

            String msg = messages.get(new java.util.Random().nextInt(messages.size()));

            boolean alreadyNotified = notificationRepository.findUnreadByUser(user).stream()
                    .anyMatch(n -> messages.contains(n.getMessage()));

            if (!alreadyNotified) {
                createNotification(userId, msg);
            }
        }
    }
}
