package com.journalintime.ui;

import com.journalintime.persistence.JPAUtil;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainUI extends Application {

    private static Stage primaryStage;

    @Override
    public void start(Stage stage) throws IOException {
        primaryStage = stage;
        setRoot("login");
        stage.setTitle("Journal Intime");
        stage.show();
    }

    public static void setRoot(String fxml) throws IOException {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MainUI.class.getResource("/fxml/" + fxml + ".fxml"));
            Parent root = fxmlLoader.load();

            applyUserPreferences(root);

            if (primaryStage.getScene() == null) {
                Scene scene = new Scene(root);
                primaryStage.setScene(scene);
            } else {
                primaryStage.getScene().setRoot(root);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    private static void applyUserPreferences(Parent root) {
        com.journalintime.dto.UserDTO user = SessionContext.getInstance().getCurrentUser();
        com.journalintime.model.enums.ThemeMode theme;
        com.journalintime.model.enums.AppFont font;

        if (user != null) {
            theme = user.getThemeMode();
            font = user.getPreferredFont();
        } else {
            java.util.prefs.Preferences prefs = java.util.prefs.Preferences.userNodeForPackage(MainUI.class);
            try {
                theme = com.journalintime.model.enums.ThemeMode.valueOf(prefs.get("theme", "LIGHT"));
            } catch (Exception e) {
                theme = com.journalintime.model.enums.ThemeMode.LIGHT;
            }
            try {
                font = com.journalintime.model.enums.AppFont.valueOf(prefs.get("font", "STANDARD"));
            } catch (Exception e) {
                font = com.journalintime.model.enums.AppFont.STANDARD;
            }
        }

        root.getStyleClass().removeAll("light", "dark");
        if (theme == com.journalintime.model.enums.ThemeMode.DARK) {
            root.getStyleClass().add("dark");
        } else {
            root.getStyleClass().add("light");
        }

        root.getStyleClass().removeAll("font-standard", "font-modern", "font-classic");
        String fontClass = switch (font != null ? font : com.journalintime.model.enums.AppFont.STANDARD) {
            case MODERN -> "font-modern";
            case CLASSIC -> "font-classic";
            default -> "font-standard";
        };
        root.getStyleClass().add(fontClass);
    }

    public static Scene getScene() {
        return primaryStage.getScene();
    }

    @Override
    public void stop() {
        JPAUtil.shutdown();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
