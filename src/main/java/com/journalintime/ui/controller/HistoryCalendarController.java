package com.journalintime.ui.controller;

import com.journalintime.service.NoteService;
import com.journalintime.dto.NoteDTO;
import com.journalintime.dto.UserDTO;
import com.journalintime.ui.MainUI;
import com.journalintime.ui.SessionContext;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class HistoryCalendarController {

    @FXML
    private Label monthLabel;
    @FXML
    private GridPane calendarGrid;
    @FXML
    private Label headerTitle;

    private YearMonth currentYearMonth;
    private NoteService noteService;
    private Map<LocalDate, List<NoteDTO>> notesByDate;

    @FXML
    public void initialize() {
        noteService = new NoteService();
        currentYearMonth = YearMonth.now();
        applyLocalization();
        loadNotes();
        drawCalendar();
    }

    private void loadNotes() {
        Long userId = SessionContext.getInstance().getCurrentUser().getId();
        List<NoteDTO> allNotes = noteService.getUserNotes(userId);
        notesByDate = allNotes.stream()
                .collect(Collectors.groupingBy(note -> note.getCreatedAt().toLocalDate()));
    }

    private void drawCalendar() {
        calendarGrid.getChildren().clear();
        monthLabel.setText(currentYearMonth.format(DateTimeFormatter.ofPattern("MMMM yyyy")));

        String[] days = { "Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun" };
        for (int i = 0; i < days.length; i++) {
            Label dayName = new Label(days[i]);
            dayName.setStyle("-fx-font-weight: bold; -fx-text-fill: #555;");
            calendarGrid.add(dayName, i, 0);
        }

        LocalDate calendarDate = LocalDate.of(currentYearMonth.getYear(), currentYearMonth.getMonth(), 1);
        int dayOfWeek = calendarDate.getDayOfWeek().getValue();

        int daysInMonth = currentYearMonth.lengthOfMonth();
        int row = 1;
        int col = dayOfWeek - 1;

        for (int i = 1; i <= daysInMonth; i++) {
            if (col > 6) {
                col = 0;
                row++;
            }

            LocalDate date = LocalDate.of(currentYearMonth.getYear(), currentYearMonth.getMonth(), i);
            VBox dayCell = createDayCell(date);
            calendarGrid.add(dayCell, col, row);

            col++;
        }
    }

    private VBox createDayCell(LocalDate date) {
        VBox cell = new VBox(5);
        cell.setAlignment(Pos.CENTER);
        cell.setPrefSize(40, 40);
        cell.getStyleClass().add("calendar-day");

        Label dayNum = new Label(String.valueOf(date.getDayOfMonth()));
        cell.getChildren().add(dayNum);

        if (notesByDate.containsKey(date)) {
            Label dot = new Label("•");
            dot.setStyle("-fx-text-fill: -color-accent; -fx-font-size: 14px;");
            cell.getChildren().add(dot);
            cell.getStyleClass().add("has-notes");
        }

        cell.setOnMouseClicked(e -> handleDateSelection(date));

        if (date.equals(LocalDate.now())) {
            cell.getStyleClass().add("today");
        }

        return cell;
    }

    private void handleDateSelection(LocalDate date) {
        SessionContext.getInstance().setSelectedDate(date);
        try {
            MainUI.setRoot("day_details");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handlePreviousMonth() {
        currentYearMonth = currentYearMonth.minusMonths(1);
        drawCalendar();
    }

    @FXML
    private void handleNextMonth() {
        currentYearMonth = currentYearMonth.plusMonths(1);
        drawCalendar();
    }

    private void applyLocalization() {
        UserDTO user = SessionContext.getInstance().getCurrentUser();
        com.journalintime.model.enums.AppLanguage lang = (user != null) ? user.getLanguage()
                : com.journalintime.model.enums.AppLanguage.EN;

        if (headerTitle != null)
            headerTitle.setText(lang == com.journalintime.model.enums.AppLanguage.FR ? "Calendrier & Historique"
                    : "Calendar & History");
    }

    @FXML
    private void handleBack() throws IOException {
        MainUI.setRoot("main_dashboard");
    }
}
