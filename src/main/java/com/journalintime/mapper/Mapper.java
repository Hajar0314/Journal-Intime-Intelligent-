package com.journalintime.mapper;

import com.journalintime.model.*;
import com.journalintime.dto.*;

import java.util.stream.Collectors;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class Mapper {

    public static UserDTO toDTO(User user) {
        if (user == null)
            return null;
        return UserDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .avatarPath(user.getAvatarPath())
                .themeMode(user.getThemeMode())
                .language(user.getLanguage())
                .preferredFont(user.getPreferredFont())
                .createdAt(user.getCreatedAt())
                .build();
    }

    public static NoteDTO toDTO(Note note) {
        if (note == null)
            return null;
        return NoteDTO.builder()
                .id(note.getId())
                .title(note.getTitle())
                .content(note.getContent())
                .createdAt(note.getCreatedAt())
                .updatedAt(note.getUpdatedAt())
                .favorite(note.isFavorite())
                .important(note.isImportant())
                .userId(note.getUser() != null ? note.getUser().getId() : null)
                .moodAnalysis(toDTO(note.getMoodAnalysis()))
                .tags(getTagsSafe(note))
                .build();
    }

    private static Set<NoteTagDTO> getTagsSafe(Note note) {
        try {
            if (note.getTags() != null) {
                return note.getTags().stream().map(Mapper::toDTO).collect(Collectors.toSet());
            }
            if (note.getTags() != null) {
                return note.getTags().stream().map(Mapper::toDTO).collect(Collectors.toSet());
            }
        } catch (Exception e) {
            return Collections.emptySet();
        }
        return Collections.emptySet();
    }

    public static NoteTagDTO toDTO(NoteTag tag) {
        if (tag == null)
            return null;
        return NoteTagDTO.builder()
                .id(tag.getId())
                .name(tag.getName())
                .color(tag.getColor())
                .build();
    }

    public static MoodAnalysisDTO toDTO(MoodAnalysis analysis) {
        if (analysis == null)
            return null;
        return MoodAnalysisDTO.builder()
                .id(analysis.getId())
                .overallMood(analysis.getOverallMood())
                .score(analysis.getScore())
                .summary(analysis.getSummary())
                .suggestedExercises(getSuggestedExercisesSafe(analysis))
                .build();
    }

    private static List<ExerciseDTO> getSuggestedExercisesSafe(MoodAnalysis analysis) {
        try {
            if (analysis.getSuggestedExercises() != null) {
                return analysis.getSuggestedExercises().stream().map(Mapper::toDTO).collect(Collectors.toList());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
        return Collections.emptyList();
    }

    public static ExerciseDTO toDTO(Exercise exercise) {
        if (exercise == null)
            return null;
        return ExerciseDTO.builder()
                .id(exercise.getId())
                .title(exercise.getTitle())
                .description(exercise.getDescription())
                .type(exercise.getType())
                .durationMinutes(exercise.getDurationMinutes())
                .build();
    }

    public static NotificationDTO toDTO(Notification notification) {
        if (notification == null)
            return null;
        return NotificationDTO.builder()
                .id(notification.getId())
                .message(notification.getMessage())
                .read(notification.isRead())
                .createdAt(notification.getTimestamp())
                .build();
    }
}
