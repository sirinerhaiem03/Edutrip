package tn.EduTrip.services;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ServiceAnnouncement {
    private static ServiceAnnouncement instance;
    private final StringProperty announcementText = new SimpleStringProperty("");

    private ServiceAnnouncement() {}

    public static ServiceAnnouncement getInstance() {
        if (instance == null) {
            instance = new ServiceAnnouncement();
        }
        return instance;
    }

    public StringProperty announcementProperty() {
        return announcementText;
    }

    public void setAnnouncement(String message) {
        Platform.runLater(() -> announcementText.set(message));
    }
}
