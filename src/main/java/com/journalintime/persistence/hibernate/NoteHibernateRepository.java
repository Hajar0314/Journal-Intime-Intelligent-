package com.journalintime.persistence.hibernate;

import com.journalintime.model.Note;
import com.journalintime.model.User;
import com.journalintime.repository.NoteRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;

public class NoteHibernateRepository extends AbstractRepository<Note, Long> implements NoteRepository {

    public NoteHibernateRepository() {
        super(Note.class);
    }

    @Override
    public List<Note> findByUser(User user) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Note> query = em
                    .createQuery(
                            "SELECT DISTINCT n FROM Note n LEFT JOIN FETCH n.user LEFT JOIN FETCH n.tags LEFT JOIN FETCH n.moodAnalysis WHERE n.user = :user ORDER BY n.createdAt DESC",
                            Note.class);
            query.setParameter("user", user);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
}
