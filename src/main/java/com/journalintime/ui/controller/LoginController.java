package com.journalintime.ui.controller;

import com.journalintime.ui.MainUI;
import com.journalintime.ui.viewmodel.LoginViewModel;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;

public class LoginController {

    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label errorLabel;
    @FXML
    private CheckBox rememberMeCheckbox;
    @FXML
    private Label appTitleLabel, loginHeaderLabel;
    @FXML
    private Button loginBtn;
    @FXML
    private Hyperlink registerLink;

    private LoginViewModel viewModel;

    @FXML
    public void initialize() {
        viewModel = new LoginViewModel();
        usernameField.textProperty().bindBidirectional(viewModel.usernameProperty());
        passwordField.textProperty().bindBidirectional(viewModel.passwordProperty());
        rememberMeCheckbox.selectedProperty().bindBidirectional(viewModel.rememberMeProperty());

        applyLocalization();
        errorLabel.textProperty().bind(viewModel.errorMessageProperty());
    }

    private void applyLocalization() {
        try {
            java.util.prefs.Preferences prefs = java.util.prefs.Preferences
                    .userNodeForPackage(com.journalintime.ui.MainUI.class);
            String langStr = prefs.get("language", "EN");
            com.journalintime.model.enums.AppLanguage lang;
            try {
                lang = com.journalintime.model.enums.AppLanguage.valueOf(langStr);
            } catch (Exception e) {
                lang = com.journalintime.model.enums.AppLanguage.EN;
            }

            appTitleLabel
                    .setText(com.journalintime.service.LocalizationService.getString("app.title", lang));
            loginHeaderLabel
                    .setText(com.journalintime.service.LocalizationService.getString("login.title", lang));
            usernameField.setPromptText(
                    com.journalintime.service.LocalizationService.getString("login.username", lang));
            passwordField.setPromptText(
                    com.journalintime.service.LocalizationService.getString("login.password", lang));
            rememberMeCheckbox.setText(
                    com.journalintime.service.LocalizationService.getString("login.remember", lang));
            loginBtn.setText(com.journalintime.service.LocalizationService.getString("login.btn", lang));
            registerLink.setText(
                    com.journalintime.service.LocalizationService.getString("login.noaccount", lang));
        } catch (Exception e) {
            System.err.println("Fallback localization due to: " + e.getMessage());
        }
    }

    @FXML
    private void handleLogin() throws IOException {
        if (viewModel.login()) {
            MainUI.setRoot("main_dashboard");
        }
    }

    @FXML
    private void handleGoToRegister() throws IOException {
        MainUI.setRoot("register");
    }
}
