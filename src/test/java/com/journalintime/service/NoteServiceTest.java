package com.journalintime.service;

import com.journalintime.model.Note;
import com.journalintime.model.User;
import com.journalintime.repository.NoteRepository;
import com.journalintime.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class NoteServiceTest {

    @Mock
    private NoteRepository noteRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private NoteService noteService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetUserNotes() {
        User user = new User();
        user.setId(1L);

        Note note1 = new Note();
        note1.setTitle("Test Note 1");
        note1.setUser(user);

        Note note2 = new Note();
        note2.setTitle("Test Note 2");
        note2.setUser(user);

        when(userRepository.findById(1L)).thenReturn(java.util.Optional.of(user));
        when(noteRepository.findByUser(user)).thenReturn(Arrays.asList(note1, note2));

        var result = noteService.getUserNotes(1L);

        assertEquals(2, result.size());
        assertEquals("Test Note 1", result.get(0).getTitle());
        verify(noteRepository, times(1)).findByUser(user);
    }
}
