package com.journalintime.repository;

import com.journalintime.model.Notification;
import com.journalintime.model.User;
import java.util.List;

public interface NotificationRepository extends Repository<Notification, Long> {
    List<Notification> findUnreadByUser(User user);
}
