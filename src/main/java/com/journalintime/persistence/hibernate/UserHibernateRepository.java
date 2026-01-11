package com.journalintime.persistence.hibernate;

import com.journalintime.model.User;
import com.journalintime.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.Optional;

public class UserHibernateRepository extends AbstractRepository<User, Long> implements UserRepository {

    public UserHibernateRepository() {
        super(User.class);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<User> query = em.createQuery("SELECT u FROM User u WHERE u.username = :username", User.class);
            query.setParameter("username", username);
            return query.getResultList().stream().findFirst();
        } finally {
            em.close();
        }
    }

    @Override
    public Optional<User> findByEmail(String email) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<User> query = em.createQuery("SELECT u FROM User u WHERE u.email = :email", User.class);
            query.setParameter("email", email);
            return query.getResultList().stream().findFirst();
        } finally {
            em.close();
        }
    }
}
