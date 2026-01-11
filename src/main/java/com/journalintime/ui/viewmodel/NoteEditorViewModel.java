package com.journalintime.ui.viewmodel;

import com.journalintime.service.NoteService;
import com.journalintime.dto.MoodAnalysisDTO;
import com.journalintime.dto.NoteDTO;
import com.journalintime.ui.SessionContext;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class NoteEditorViewModel {
    private final NoteService noteService;
    private final StringProperty title = new SimpleStringProperty();
    private final StringProperty content = new SimpleStringProperty();
    private final BooleanProperty favorite = new SimpleBooleanProperty();
    private final BooleanProperty important = new SimpleBooleanProperty();
    private final ObjectProperty<MoodAnalysisDTO> analysis = new SimpleObjectProperty<>();
    private NoteDTO currentNote;

    public NoteEditorViewModel() {
        this.noteService = new NoteService();
        this.currentNote = SessionContext.getInstance().getCurrentNote();
        if (currentNote != null) {
            title.set(currentNote.getTitle());
            content.set(currentNote.getContent());
            favorite.set(currentNote.isFavorite());
            important.set(currentNote.isImportant());
            updateAnalysis(currentNote.getMoodAnalysis());
        }
    }

    public BooleanProperty favoriteProperty() {
        return favorite;
    }

    public BooleanProperty importantProperty() {
        return important;
    }

    public StringProperty titleProperty() {
        return title;
    }

    public StringProperty contentProperty() {
        return content;
    }

    public ObjectProperty<MoodAnalysisDTO> analysisProperty() {
        return analysis;
    }

    public NoteDTO getCurrentNote() {
        return currentNote;
    }

    public boolean save() {
        try {
            Long userId = SessionContext.getInstance().getCurrentUser().getId();
            NoteDTO saved;
            if (currentNote == null) {
                saved = noteService.createNote(userId, title.get(), content.get(), favorite.get(), important.get());
            } else {
                saved = noteService.updateNote(currentNote.getId(), title.get(), content.get(), favorite.get(),
                        important.get());
            }
            currentNote = saved;
            updateAnalysis(saved.getMoodAnalysis());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete() {
        if (currentNote != null) {
            try {
                noteService.deleteNote(currentNote.getId());
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    private void updateAnalysis(MoodAnalysisDTO analysisDTO) {
        this.analysis.set(analysisDTO);
    }
}
