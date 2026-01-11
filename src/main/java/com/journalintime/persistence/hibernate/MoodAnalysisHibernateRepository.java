package com.journalintime.persistence.hibernate;

import com.journalintime.model.MoodAnalysis;
import com.journalintime.repository.MoodAnalysisRepository;

public class MoodAnalysisHibernateRepository extends AbstractRepository<MoodAnalysis, Long>
        implements MoodAnalysisRepository {
    public MoodAnalysisHibernateRepository() {
        super(MoodAnalysis.class);
    }
}
