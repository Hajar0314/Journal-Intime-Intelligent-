package com.journalintime.persistence.hibernate;

import com.journalintime.model.Notification;
import com.journalintime.model.User;
import com.journalintime.repository.NotificationRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;

public class NotificationHibernateRepository extends AbstractRepository<Notification, Long>
        implements NotificationRepository {

    public NotificationHibernateRepository() {
        super(Notification.class);
    }

    @Override
    public List<Notification> findUnreadByUser(User user) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Notification> query = em.createQuery(
                    "SELECT n FROM Notification n WHERE n.user = :user AND n.read = false ORDER BY n.timestamp DESC",
                    Notification.class);
            query.setParameter("user", user);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
}
