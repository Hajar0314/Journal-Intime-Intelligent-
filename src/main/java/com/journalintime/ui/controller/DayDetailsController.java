package com.journalintime.ui.controller;

import com.journalintime.service.NoteService;
import com.journalintime.service.LocalizationService;
import com.journalintime.dto.NoteDTO;
import com.journalintime.dto.UserDTO;
import com.journalintime.ui.MainUI;
import com.journalintime.ui.SessionContext;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class DayDetailsController {

    @FXML
    private Label headerLabel, dateLabel, dayScoreLabel, scoreValueLabel, entriesCountLabel, countValueLabel,
            listTitleLabel;
    @FXML
    private ListView<NoteDTO> dayNotesListView;
    @FXML
    private Button backBtn;

    private NoteService noteService;
    private LocalDate selectedDate;

    @FXML
    public void initialize() {
        noteService = new NoteService();
        selectedDate = SessionContext.getInstance().getSelectedDate();

        applyLocalization();
        loadDayData();

        dayNotesListView.setCellFactory(lv -> new ListCell<NoteDTO>() {
            @Override
            protected void updateItem(NoteDTO note, boolean empty) {
                super.updateItem(note, empty);
                if (empty || note == null) {
                    setText(null);
                } else {
                    String time = (note.getCreatedAt() != null)
                            ? note.getCreatedAt().toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm"))
                            : "--:--";
                    setText("[" + time + "] " + note.getTitle());
                }
            }
        });

        dayNotesListView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                NoteDTO selected = dayNotesListView.getSelectionModel().getSelectedItem();
                if (selected != null) {
                    SessionContext.getInstance().setCurrentNote(selected);
                    try {
                        MainUI.setRoot("note_editor");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void applyLocalization() {
        UserDTO user = SessionContext.getInstance().getCurrentUser();
        com.journalintime.model.enums.AppLanguage lang = (user != null) ? user.getLanguage()
                : com.journalintime.model.enums.AppLanguage.EN;

        headerLabel.setText(LocalizationService.getString("daydetails.header", lang));
        dayScoreLabel.setText(LocalizationService.getString("daydetails.dayscore", lang));
        entriesCountLabel.setText(LocalizationService.getString("daydetails.entries", lang));
        listTitleLabel.setText(LocalizationService.getString("daydetails.listtitle", lang));
        backBtn.setText(lang == com.journalintime.model.enums.AppLanguage.FR ? "← Retour" : "← Back");
    }

    private void loadDayData() {
        if (selectedDate == null)
            return;

        UserDTO user = SessionContext.getInstance().getCurrentUser();
        com.journalintime.model.enums.AppLanguage lang = (user != null) ? user.getLanguage()
                : com.journalintime.model.enums.AppLanguage.EN;

        dateLabel.setText(selectedDate.format(DateTimeFormatter.ofPattern(
                lang == com.journalintime.model.enums.AppLanguage.FR ? "dd MMMM yyyy" : "MMMM dd, yyyy",
                lang == com.journalintime.model.enums.AppLanguage.FR ? java.util.Locale.FRENCH
                        : java.util.Locale.ENGLISH)));

        List<NoteDTO> notes = noteService.getUserNotes(user.getId()).stream()
                .filter(n -> n.getCreatedAt() != null && n.getCreatedAt().toLocalDate().equals(selectedDate))
                .collect(Collectors.toList());

        countValueLabel.setText(String.valueOf(notes.size()));

        double avgScore = notes.stream()
                .filter(n -> n.getMoodAnalysis() != null)
                .mapToDouble(n -> n.getMoodAnalysis().getScore())
                .average()
                .orElse(-1.0);

        if (avgScore >= 0) {
            scoreValueLabel.setText(String.format("%.1f/10", avgScore));
        } else {
            scoreValueLabel.setText("N/A");
        }

        ObservableList<NoteDTO> observableNotes = FXCollections.observableArrayList(notes);
        dayNotesListView.setItems(observableNotes);
    }

    @FXML
    private void handleBack() throws IOException {
        MainUI.setRoot("history_calendar");
    }
}
