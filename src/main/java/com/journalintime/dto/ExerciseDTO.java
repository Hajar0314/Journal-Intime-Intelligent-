package com.journalintime.dto;

import com.journalintime.model.enums.ExerciseType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ExerciseDTO {
    private Long id;
    private String title;
    private String description;
    private ExerciseType type;
    private Integer durationMinutes;
}
