package com.journalintime.model;

import com.journalintime.model.enums.MoodLevel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "mood_analysis")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MoodAnalysis {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "note_id", nullable = false)
    private Note note;

    @Enumerated(EnumType.STRING)
    private MoodLevel overallMood;

    private Double score;

    @Column(columnDefinition = "TEXT")
    private String summary;

    @Builder.Default
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "mood_analysis_exercises", joinColumns = @JoinColumn(name = "analysis_id"), inverseJoinColumns = @JoinColumn(name = "exercise_id"))
    private List<Exercise> suggestedExercises = new ArrayList<>();
}
