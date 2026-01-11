package com.journalintime.ui.controller;

import com.journalintime.service.LocalizationService;
import com.journalintime.model.enums.AppLanguage;
import com.journalintime.ui.MainUI;
import com.journalintime.ui.viewmodel.RegisterViewModel;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.io.IOException;
import java.util.prefs.Preferences;

public class RegisterController {

    @FXML
    private TextField usernameField;
    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label messageLabel;
    @FXML
    private Label appTitleLabel, registerHeaderLabel;
    @FXML
    private Button registerBtn, backBtn;

    private RegisterViewModel viewModel;

    @FXML
    public void initialize() {
        applyLocalization();
        viewModel = new RegisterViewModel();
        usernameField.textProperty().bindBidirectional(viewModel.usernameProperty());
        emailField.textProperty().bindBidirectional(viewModel.emailProperty());
        passwordField.textProperty().bindBidirectional(viewModel.passwordProperty());
        messageLabel.textProperty().bind(viewModel.messageProperty());
    }

    private void applyLocalization() {
        Preferences prefs = Preferences.userNodeForPackage(MainUI.class);
        AppLanguage lang = AppLanguage.valueOf(prefs.get("language", "EN"));

        appTitleLabel.setText(LocalizationService.getString("app.title", lang));
        registerHeaderLabel.setText(LocalizationService.getString("register.title", lang));
        usernameField.setPromptText(LocalizationService.getString("login.username", lang));
        emailField.setPromptText(LocalizationService.getString("register.email", lang));
        passwordField.setPromptText(LocalizationService.getString("login.password", lang));
        registerBtn.setText(LocalizationService.getString("register.btn", lang));
        backBtn.setText(LocalizationService.getString("register.back", lang));
    }

    @FXML
    private void handleRegister() {
        if (viewModel.register()) {
        }
    }

    @FXML
    private void handleBackToLogin() throws IOException {
        MainUI.setRoot("login");
    }
}
