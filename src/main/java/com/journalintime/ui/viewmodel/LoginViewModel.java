package com.journalintime.ui.viewmodel;

import com.journalintime.service.UserService;
import com.journalintime.dto.UserDTO;
import com.journalintime.ui.SessionContext;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import java.util.prefs.Preferences;

public class LoginViewModel {
    private final UserService userService;
    private final StringProperty username = new SimpleStringProperty();
    private final StringProperty password = new SimpleStringProperty();
    private final StringProperty errorMessage = new SimpleStringProperty();
    private final BooleanProperty rememberMe = new SimpleBooleanProperty();
    private final Preferences prefs = Preferences.userNodeForPackage(LoginViewModel.class);

    public LoginViewModel() {
        this.userService = new UserService();
        loadCredentials();
    }

    public StringProperty usernameProperty() {
        return username;
    }

    public StringProperty passwordProperty() {
        return password;
    }

    public StringProperty errorMessageProperty() {
        return errorMessage;
    }

    public BooleanProperty rememberMeProperty() {
        return rememberMe;
    }

    public boolean login() {
        if (username.get() == null || username.get().trim().isEmpty()) {
            errorMessage.set("Username cannot be empty");
            return false;
        }
        if (password.get() == null || password.get().isEmpty()) {
            errorMessage.set("Password cannot be empty");
            return false;
        }
        try {
            UserDTO user = userService.login(username.get(), password.get());
            SessionContext.getInstance().setCurrentUser(user);
            saveCredentials();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            errorMessage.set(e.getMessage());
            return false;
        }
    }

    private void saveCredentials() {
        try {
            if (rememberMe.get()) {
                prefs.put("username", username.get());
                prefs.putBoolean("rememberMe", true);
            } else {
                prefs.remove("username");
                prefs.putBoolean("rememberMe", false);
            }
        } catch (Exception e) {
            System.err.println("Could not save credentials to preferences: " + e.getMessage());
        }
    }

    private void loadCredentials() {
        try {
            boolean remember = prefs.getBoolean("rememberMe", false);
            if (remember) {
                username.set(prefs.get("username", ""));
                rememberMe.set(true);
            }
        } catch (Exception e) {
            System.err.println("Could not load credentials from preferences: " + e.getMessage());
        }
    }
}
