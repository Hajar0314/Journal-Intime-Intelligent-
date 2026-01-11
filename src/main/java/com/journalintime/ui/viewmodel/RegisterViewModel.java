package com.journalintime.ui.viewmodel;

import com.journalintime.service.UserService;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class RegisterViewModel {
    private final UserService userService;
    private final StringProperty username = new SimpleStringProperty();
    private final StringProperty email = new SimpleStringProperty();
    private final StringProperty password = new SimpleStringProperty();
    private final StringProperty message = new SimpleStringProperty();

    public RegisterViewModel() {
        this.userService = new UserService();
    }

    public StringProperty usernameProperty() {
        return username;
    }

    public StringProperty emailProperty() {
        return email;
    }

    public StringProperty passwordProperty() {
        return password;
    }

    public StringProperty messageProperty() {
        return message;
    }

    public boolean register() {
        try {
            userService.register(username.get(), email.get(), password.get());
            message.set("Registration successful! Please login.");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            message.set("Error: " + e.getMessage());
            return false;
        }
    }
}
