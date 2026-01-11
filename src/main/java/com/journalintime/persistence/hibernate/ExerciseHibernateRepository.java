package com.journalintime.persistence.hibernate;

import com.journalintime.model.Exercise;
import com.journalintime.repository.ExerciseRepository;

public class ExerciseHibernateRepository extends AbstractRepository<Exercise, Long> implements ExerciseRepository {
    public ExerciseHibernateRepository() {
        super(Exercise.class);
    }
}
