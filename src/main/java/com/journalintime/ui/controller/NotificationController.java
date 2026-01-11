package com.journalintime.ui.controller;

import com.journalintime.service.NotificationService;
import com.journalintime.dto.NotificationDTO;
import com.journalintime.ui.MainUI;
import com.journalintime.ui.SessionContext;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class NotificationController {

    @FXML
    private ListView<NotificationDTO> notificationListView;

    private NotificationService notificationService;
    private final ObservableList<NotificationDTO> notifications = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        notificationService = new NotificationService();
        setupListView();
        loadNotifications();
    }

    private void setupListView() {
        notificationListView.setCellFactory(lv -> new ListCell<NotificationDTO>() {
            @Override
            protected void updateItem(NotificationDTO item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    VBox card = new VBox(5);
                    card.getStyleClass().add("notification-card");
                    if (!item.isRead()) {
                        card.getStyleClass().add("unread");
                    }

                    Label message = new Label(item.getMessage());
                    message.setWrapText(true);
                    message.getStyleClass().add("notification-message");

                    Label date = new Label(
                            item.getCreatedAt().format(DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm")));
                    date.getStyleClass().add("notification-date");

                    card.getChildren().addAll(message, date);

                    card.setOnMouseClicked(e -> {
                        if (!item.isRead()) {
                            notificationService.markAsRead(item.getId());
                            item.setRead(true);
                            card.getStyleClass().remove("unread");
                        }
                    });

                    setGraphic(card);
                }
            }
        });
        notificationListView.setItems(notifications);
    }

    private void loadNotifications() {
        Long userId = SessionContext.getInstance().getCurrentUser().getId();
        List<NotificationDTO> unread = notificationService.getUnreadNotifications(userId);
        notifications.setAll(unread);
    }

    @FXML
    private void handleBack() throws IOException {
        MainUI.setRoot("main_dashboard");
    }

    @FXML
    private void handleMarkAllRead() {
        notifications.forEach(n -> {
            if (!n.isRead()) {
                notificationService.markAsRead(n.getId());
                n.setRead(true);
            }
        });
        notificationListView.refresh();
    }
}
