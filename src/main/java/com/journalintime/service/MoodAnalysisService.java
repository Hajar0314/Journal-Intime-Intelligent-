package com.journalintime.service;

import com.journalintime.model.MoodAnalysis;
import com.journalintime.model.Note;
import com.journalintime.model.enums.MoodLevel;

public class MoodAnalysisService {

    public void analyze(Note note) {
        String content = note.getContent().toLowerCase();
        MoodLevel mood = MoodLevel.NEUTRAL;
        double score = 0.0;
        String summary = "Neutral mood detected.";

        if (content.contains("happy") || content.contains("joy") || content.contains("great")
                || content.contains("amazing") || content.contains("excited")) {
            mood = MoodLevel.VERY_GOOD;
            score = 0.95;
            summary = "You're glowing today! Keep this wonderful energy going.";
        } else if (content.contains("good") || content.contains("pleasant") || content.contains("satisfied")) {
            mood = MoodLevel.GOOD;
            score = 0.7;
            summary = "A solid, positive day. Well done!";
        } else if (content.contains("sad") || content.contains("unhappy") || content.contains("depressed")) {
            mood = MoodLevel.VERY_BAD;
            score = 0.1;
            summary = "It sounds like you're going through a very difficult time. Be kind to yourself today.";
        } else if (content.contains("bad") || content.contains("angry") || content.contains("frustrated")) {
            mood = MoodLevel.BAD;
            score = 0.3;
            summary = "Seems like a tough day with some heavy emotions.";
        } else if (content.contains("anxious") || content.contains("stressed") || content.contains("worried")) {
            mood = MoodLevel.BAD;
            score = 0.45;
            summary = "You seem a bit stressed or anxious. Take a deep breath.";
        }

        MoodAnalysis analysis = note.getMoodAnalysis();
        if (analysis == null) {
            analysis = MoodAnalysis.builder()
                    .note(note)
                    .overallMood(mood)
                    .score(score)
                    .summary(summary)
                    .build();
            note.setMoodAnalysis(analysis);
        } else {
            analysis.setOverallMood(mood);
            analysis.setScore(score);
            analysis.setSummary(summary);
        }

        java.util.List<com.journalintime.model.Exercise> exercises = new java.util.ArrayList<>();

        if (content.contains("stress") || content.contains("anxiety") || content.contains("overwhelmed")
                || content.contains("pressure")) {
            exercises.add(createExercise("Box Breathing", "Inhale 4s, Hold 4s, Exhale 4s, Hold 4s.",
                    com.journalintime.model.enums.ExerciseType.BREATHING, 5));
        }
        if (content.contains("tired") || content.contains("exhausted") || content.contains("sleep")) {
            exercises.add(createExercise("Power Nap / Rest", "Close your eyes for 15-20 min or practice Yoga Nidra.",
                    com.journalintime.model.enums.ExerciseType.MEDITATION, 20));
        }
        if (content.contains("lonely") || content.contains("alone") || content.contains("sad")) {
            exercises
                    .add(createExercise("Connection Call", "Reach out to one friend or family member for a quick chat.",
                            com.journalintime.model.enums.ExerciseType.MEDITATION, 15));
        }
        if (content.contains("nature") || content.contains("outside") || content.contains("park")) {
            exercises.add(createExercise("Mindful Walk", "Go outside and focus solely on the sensation of walking.",
                    com.journalintime.model.enums.ExerciseType.MEDITATION, 15));
        }

        if (mood == MoodLevel.BAD || mood == MoodLevel.VERY_BAD) {
            exercises.add(createExercise("5-4-3-2-1 Grounding",
                    "Acknowledge 5 things you see, 4 feel, 3 hear, 2 smell, 1 taste.",
                    com.journalintime.model.enums.ExerciseType.MEDITATION, 10));
            exercises.add(
                    createExercise("Expressive Writing", "Write continuously for 10 min about your deepest thoughts.",
                            com.journalintime.model.enums.ExerciseType.JOURNALING, 10));
            if (exercises.size() < 3) {
                exercises.add(createExercise("Gentle Stretching", "Release physical tension in neck and shoulders.",
                        com.journalintime.model.enums.ExerciseType.MEDITATION, 5));
            }
        } else if (mood == MoodLevel.GOOD || mood == MoodLevel.VERY_GOOD) {
            exercises.add(createExercise("Gratitude List", "Write down 5 things you're grateful for right now.",
                    com.journalintime.model.enums.ExerciseType.JOURNALING, 5));
            exercises.add(createExercise("Share the Joy", "Tell someone about the highlight of your day.",
                    com.journalintime.model.enums.ExerciseType.MEDITATION, 5));
            summary += " You're doing great! Keep up the positive energy.";
        } else {
            // Neutral
            exercises
                    .add(createExercise("Mindful Observation", "Pick an object and observe it in detail for 5 minutes.",
                            com.journalintime.model.enums.ExerciseType.MEDITATION, 5));
        }

        analysis.setSuggestedExercises(exercises);
        analysis.setSummary(summary);
    }

    private com.journalintime.model.Exercise createExercise(String title, String description,
            com.journalintime.model.enums.ExerciseType type, int duration) {
        return com.journalintime.model.Exercise.builder()
                .title(title)
                .description(description)
                .type(type)
                .durationMinutes(duration)
                .build();
    }
}
