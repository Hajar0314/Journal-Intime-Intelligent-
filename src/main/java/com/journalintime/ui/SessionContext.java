package com.journalintime.ui;

import com.journalintime.dto.UserDTO;

public class SessionContext {
    private static SessionContext instance;
    private UserDTO currentUser;

    private SessionContext() {
    }

    public static SessionContext getInstance() {
        if (instance == null) {
            instance = new SessionContext();
        }
        return instance;
    }

    public void setCurrentUser(UserDTO user) {
        this.currentUser = user;
    }

    public UserDTO getCurrentUser() {
        return currentUser;
    }

    private com.journalintime.dto.NoteDTO currentNote;

    public void setCurrentNote(com.journalintime.dto.NoteDTO note) {
        this.currentNote = note;
    }

    public com.journalintime.dto.NoteDTO getCurrentNote() {
        return currentNote;
    }

    private java.time.LocalDate selectedDate;

    public void setSelectedDate(java.time.LocalDate date) {
        this.selectedDate = date;
    }

    public java.time.LocalDate getSelectedDate() {
        return selectedDate;
    }

    public void logout() {
        this.currentUser = null;
        this.currentNote = null;
        this.selectedDate = null;
    }

    public void clear() {
        logout();
    }
}
