package com.journalintime.ui.controller;

import com.journalintime.model.enums.AppLanguage;
import com.journalintime.dto.UserDTO;
import com.journalintime.ui.MainUI;
import com.journalintime.ui.SessionContext;
import com.journalintime.service.UserService;
import com.journalintime.service.LocalizationService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.io.IOException;

public class ProfileSettingsController {

    @FXML
    private TextField usernameField;
    @FXML
    private TextField emailField;
    @FXML
    private Label headerLabel, userInfoLabel, usernameLabel, emailLabel;
    @FXML
    private Button updateProfileBtn, saveAllBtn, backBtn;

    private UserService userService;
    private UserDTO currentUser;

    @FXML
    public void initialize() {
        userService = new UserService();
        currentUser = SessionContext.getInstance().getCurrentUser();

        if (currentUser != null) {
            usernameField.setText(currentUser.getUsername());
            emailField.setText(currentUser.getEmail());
            applyLocalization(currentUser.getLanguage());
        }
    }

    private void applyLocalization(AppLanguage lang) {
        headerLabel.setText(LocalizationService.getString("profile.header", lang));
        userInfoLabel.setText(LocalizationService.getString("profile.userInfo", lang));
        usernameLabel.setText(LocalizationService.getString("profile.username", lang));
        emailLabel.setText(LocalizationService.getString("profile.email", lang));
        updateProfileBtn.setText(LocalizationService.getString("profile.update", lang));
        saveAllBtn.setText(LocalizationService.getString("profile.save", lang));
        backBtn.setText(lang == AppLanguage.FR ? "← Retour" : "← Back");
    }

    @FXML
    private void handleUpdateProfile() {
        handleSaveAll();
    }

    @FXML
    private void handleSaveAll() {
        try {
            if (currentUser == null)
                return;

            currentUser.setEmail(emailField.getText());
            currentUser.setUsername(usernameField.getText());

            UserDTO updated = userService.updateUserPreferences(currentUser);
            SessionContext.getInstance().setCurrentUser(updated);

            applyLocalization(updated.getLanguage());
            showAlert("Settings Saved", "Your profile information has been updated.");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Could not save profile: " + e.getMessage());
        }
    }

    @FXML
    private void handleBack() throws IOException {
        MainUI.setRoot("main_dashboard");
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
