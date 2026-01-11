package com.journalintime.ui.controller;

import com.journalintime.dto.NoteDTO;
import com.journalintime.dto.UserDTO;
import com.journalintime.ui.MainUI;
import com.journalintime.ui.SessionContext;
import com.journalintime.service.NotificationService;
import com.journalintime.service.LocalizationService;
import com.journalintime.ui.viewmodel.MainDashboardViewModel;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import com.journalintime.model.enums.AppLanguage;
import com.journalintime.model.enums.ThemeMode;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import java.util.prefs.Preferences;

import java.io.IOException;

public class MainDashboardController {

    @FXML
    private ListView<NoteDTO> notesListView;
    @FXML
    private TextField searchField;
    @FXML
    private Label notifBadge;
    @FXML
    private CheckBox favoriteFilter;
    @FXML
    private Label sectionHeader;
    @FXML
    private Button analyticsBtn, calendarBtn, profileBtn, logoutBtn;
    @FXML
    private Label appTitleLabel, settingsLabel, langLabel;
    @FXML
    private ComboBox<com.journalintime.model.enums.AppLanguage> languageCombo;
    @FXML
    private ToggleButton darkModeToggle;

    private MainDashboardViewModel viewModel;
    private NotificationService notificationService;

    @FXML
    public void initialize() {
        viewModel = new MainDashboardViewModel();
        notificationService = new NotificationService();

        FilteredList<NoteDTO> filteredData = new FilteredList<>(viewModel.getNotes(), p -> true);

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            updateFilter(filteredData);
        });

        favoriteFilter.selectedProperty().addListener((obs, oldV, newV) -> {
            updateFilter(filteredData);
        });

        notesListView.setCellFactory(lv -> new ListCell<NoteDTO>() {
            @Override
            protected void updateItem(NoteDTO note, boolean empty) {
                super.updateItem(note, empty);
                if (empty || note == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    String prefix = "";
                    if (note.isFavorite())
                        prefix += "⭐ ";
                    if (note.isImportant())
                        prefix += "🚩 ";
                    String dateStr = (note.getCreatedAt() != null) ? note.getCreatedAt().toLocalDate().toString()
                            : "Unknown date";
                    setText(prefix + note.getTitle() + " (" + dateStr + ")");
                }
            }
        });

        notesListView.setItems(filteredData);
        viewModel.loadNotes();
        if (SessionContext.getInstance().getCurrentUser() != null) {
            notificationService.checkAndCreateReminders(SessionContext.getInstance().getCurrentUser().getId());
        }
        updateNotificationBadge();
        applyLocalization();

        UserDTO user = SessionContext.getInstance().getCurrentUser();
        if (user != null) {
            languageCombo.setItems(FXCollections
                    .observableArrayList(AppLanguage.values()));
            languageCombo.setValue(user.getLanguage());
            darkModeToggle.setSelected(user.getThemeMode() == ThemeMode.DARK);

            languageCombo.valueProperty().addListener((obs, oldV, newV) -> {
                if (newV != null)
                    handleLanguageChange(newV);
            });
        }
    }

    private void handleLanguageChange(AppLanguage newLang) {
        UserDTO user = SessionContext.getInstance().getCurrentUser();
        if (user != null) {
            user.setLanguage(newLang);
            new com.journalintime.service.UserService().updateUserPreferences(user);

            Preferences prefs = Preferences.userNodeForPackage(MainUI.class);
            prefs.put("language", newLang.name());

            applyLocalization();
        }
    }

    @FXML
    private void handleThemeToggle() {
        UserDTO user = SessionContext.getInstance().getCurrentUser();
        if (user != null) {
            ThemeMode mode = darkModeToggle.isSelected()
                    ? ThemeMode.DARK
                    : ThemeMode.LIGHT;
            user.setThemeMode(mode);
            new com.journalintime.service.UserService().updateUserPreferences(user);

            Preferences prefs = Preferences.userNodeForPackage(MainUI.class);
            prefs.put("theme", mode.name());

            if (mode == ThemeMode.DARK) {
                MainUI.getScene().getRoot().getStyleClass().add("dark");
            } else {
                MainUI.getScene().getRoot().getStyleClass().remove("dark");
            }
        }
    }

    private void applyLocalization() {
        UserDTO user = SessionContext.getInstance().getCurrentUser();
        AppLanguage lang = (user != null) ? user.getLanguage()
                : AppLanguage.EN;

        if (sectionHeader != null)
            sectionHeader.setText(LocalizationService.getString("dashboard.notes", lang));
        if (analyticsBtn != null)
            analyticsBtn.setText(LocalizationService.getString("nav.analytics", lang));
        if (calendarBtn != null)
            calendarBtn.setText(lang == AppLanguage.FR ? "Calendrier" : "Calendar");
        if (profileBtn != null)
            profileBtn.setText(LocalizationService.getString("nav.profile", lang));
        if (logoutBtn != null)
            logoutBtn.setText(LocalizationService.getString("nav.logout", lang));
        if (favoriteFilter != null)
            favoriteFilter.setText(LocalizationService.getString("filter.favorites", lang));
        if (appTitleLabel != null)
            appTitleLabel.setText(LocalizationService.getString("app.title", lang));

        if (settingsLabel != null)
            settingsLabel
                    .setText(lang == AppLanguage.FR ? "Paramètres" : "Quick Settings");
        if (langLabel != null)
            langLabel.setText(lang == AppLanguage.FR ? "Lang:" : "Lang:");
        if (darkModeToggle != null)
            darkModeToggle.setText(lang == AppLanguage.FR ? "Mode Sombre" : "Dark Mode");
    }

    private void updateFilter(FilteredList<NoteDTO> filteredData) {
        filteredData.setPredicate(note -> {
            String searchText = searchField.getText() == null ? "" : searchField.getText().toLowerCase();
            boolean matchesSearch = note.getTitle().toLowerCase().contains(searchText);
            boolean matchesFavorite = !favoriteFilter.isSelected() || note.isFavorite();
            return matchesSearch && matchesFavorite;
        });
    }

    @FXML
    private void handleFilterChange() {
    }

    @FXML
    private void handleProfile() {
        try {
            MainUI.setRoot("profile_settings");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleAddNote() throws IOException {
        SessionContext.getInstance().setCurrentNote(null);
        MainUI.setRoot("note_editor");
    }

    @FXML
    private void handleEditNote() throws IOException {
        NoteDTO selected = notesListView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            SessionContext.getInstance().setCurrentNote(selected);
            MainUI.setRoot("note_editor");
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Selection Required");
            alert.setHeaderText(null);
            alert.setContentText("Please select a note to view/edit.");
            alert.showAndWait();
        }
    }

    @FXML
    private void handleAnalytics() throws IOException {
        MainUI.setRoot("analytics");
    }

    @FXML
    private void handleCalendar() throws IOException {
        MainUI.setRoot("history_calendar");
    }

    @FXML
    private void handleNotifications() throws IOException {
        MainUI.setRoot("notifications");
    }

    @FXML
    private void handleLogout() throws IOException {
        SessionContext.getInstance().setCurrentUser(null);
        MainUI.setRoot("login");
    }

    private void updateNotificationBadge() {
        try {
            Long userId = SessionContext.getInstance().getCurrentUser().getId();
            long count = notificationService.getUnreadCount(userId);
            if (count > 0) {
                notifBadge.setText(String.valueOf(count));
                notifBadge.setVisible(true);
            } else {
                notifBadge.setVisible(false);
            }
        } catch (Exception e) {
            notifBadge.setVisible(false);
        }
    }
}
