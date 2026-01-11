package com.journalintime.persistence.hibernate;

import com.journalintime.model.NoteTag;
import com.journalintime.repository.NoteTagRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.Optional;

public class NoteTagHibernateRepository extends AbstractRepository<NoteTag, Long> implements NoteTagRepository {

    public NoteTagHibernateRepository() {
        super(NoteTag.class);
    }

    @Override
    public Optional<NoteTag> findByName(String name) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<NoteTag> query = em.createQuery("SELECT t FROM NoteTag t WHERE t.name = :name", NoteTag.class);
            query.setParameter("name", name);
            return query.getResultList().stream().findFirst();
        } finally {
            em.close();
        }
    }
}
