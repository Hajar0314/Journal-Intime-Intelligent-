package com.journalintime.dto;

import com.journalintime.model.enums.MoodLevel;
import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class MoodAnalysisDTO {
    private Long id;
    private MoodLevel overallMood;
    private Double score;
    private String summary;
    private List<ExerciseDTO> suggestedExercises;
}
