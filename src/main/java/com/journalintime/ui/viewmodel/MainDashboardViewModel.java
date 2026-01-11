package com.journalintime.ui.viewmodel;

import com.journalintime.service.NoteService;
import com.journalintime.dto.NoteDTO;
import com.journalintime.ui.SessionContext;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class MainDashboardViewModel {
    private final NoteService noteService;
    private final ObservableList<NoteDTO> notes = FXCollections.observableArrayList();

    public MainDashboardViewModel() {
        this.noteService = new NoteService();
    }

    public ObservableList<NoteDTO> getNotes() {
        return notes;
    }

    public void loadNotes() {
        if (SessionContext.getInstance().getCurrentUser() == null) {
            System.err.println("Attempted to load notes without a logged-in user.");
            return;
        }
        Long userId = SessionContext.getInstance().getCurrentUser().getId();
        notes.setAll(noteService.getUserNotes(userId));
    }

    public void logout() {
        SessionContext.getInstance().logout();
    }
}
