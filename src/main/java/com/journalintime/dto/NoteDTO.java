package com.journalintime.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
public class NoteDTO {
    private Long id;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean favorite;
    private boolean important;
    private Long userId;
    private MoodAnalysisDTO moodAnalysis;
    private Set<NoteTagDTO> tags;
}
