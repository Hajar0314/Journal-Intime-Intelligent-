package com.journalintime.service;

import com.journalintime.model.Note;
import com.journalintime.model.User;
import com.journalintime.repository.NoteRepository;
import com.journalintime.repository.UserRepository;
import com.journalintime.persistence.hibernate.NoteHibernateRepository;
import com.journalintime.persistence.hibernate.UserHibernateRepository;
import com.journalintime.dto.NoteDTO;
import com.journalintime.mapper.Mapper;

import java.util.List;
import java.util.stream.Collectors;

public class NoteService {
    private final NoteRepository noteRepository;
    private final UserRepository userRepository;
    private final MoodAnalysisService moodAnalysisService;

    public NoteService() {
        this.noteRepository = new NoteHibernateRepository();
        this.userRepository = new UserHibernateRepository();
        this.moodAnalysisService = new MoodAnalysisService();
    }

    public NoteDTO createNote(Long userId, String title, String content, boolean favorite, boolean important) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Note note = Note.builder()
                .title(title)
                .content(content)
                .favorite(favorite)
                .important(important)
                .user(user)
                .build();

        moodAnalysisService.analyze(note);
        note = noteRepository.save(note);
        return Mapper.toDTO(note);
    }

    public List<NoteDTO> getUserNotes(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return noteRepository.findByUser(user).stream()
                .map(Mapper::toDTO)
                .collect(Collectors.toList());
    }

    public void deleteNote(Long noteId) {
        noteRepository.findById(noteId).ifPresent(noteRepository::delete);
    }

    public NoteDTO updateNote(Long noteId, String newTitle, String newContent, boolean favorite, boolean important) {
        Note note = noteRepository.findById(noteId)
                .orElseThrow(() -> new RuntimeException("Note not found"));

        note.setTitle(newTitle);
        note.setContent(newContent);
        note.setFavorite(favorite);
        note.setImportant(important);

        moodAnalysisService.analyze(note);

        return Mapper.toDTO(noteRepository.update(note));
    }
}
