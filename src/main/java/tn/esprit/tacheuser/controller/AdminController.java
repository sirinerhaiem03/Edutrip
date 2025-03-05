package tn.esprit.tacheuser.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import tn.esprit.tacheuser.models.Avis;
import tn.esprit.tacheuser.models.User;
import tn.esprit.tacheuser.services.AvisService;
import tn.esprit.tacheuser.services.UserService;

import java.io.IOException;
import java.util.List;

public class AdminController {

    @FXML
    private ListView<String> userListView;
    @FXML
    private ListView<String> reviewListView;
    @FXML
    private Button deleteUserButton;

    private final AvisService avisService = new AvisService();
    private final UserService userService = new UserService();
    private List<User> users;

    @FXML
    public void initialize() {
        loadReviews();
        loadUsers();
        deleteUserButton.setOnAction(event -> deleteUser());
    }

    private void loadReviews() {
        // Remplacez 'fetch()' par 'getAll()' selon la méthode dans AvisService
        for (Avis avis : avisService.getAll()) {
            reviewListView.getItems().add(avis.getCommentaire() + " - Note: " + avis.getNote());
        }
    }

    private void loadUsers() {
        users = userService.getAllUsers();
        for (User user : users) {
            userListView.getItems().add(user.getNom() + " " + user.getPrenom() + " - " + user.getMail());
        }
    }

    private void deleteUser() {
        int selectedIndex = userListView.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            User selectedUser = users.get(selectedIndex);
            showConfirmationAlert("Suppression d'utilisateur", "Voulez-vous vraiment supprimer " + selectedUser.getNom() + " ?", () -> {
                userService.deleteUser(selectedUser.getId());
                userListView.getItems().remove(selectedIndex);
            });
        } else {
            showWarningAlert("Aucune sélection", "Veuillez sélectionner un utilisateur à supprimer.");
        }
    }

    private void deleteReview() {
        int selectedIndex = reviewListView.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            Avis selectedReview = avisService.getAll().get(selectedIndex); // Mise à jour vers 'getAll()'
            showConfirmationAlert("Suppression de l'avis", "Voulez-vous vraiment supprimer cet avis ?", () -> {
                avisService.delete(selectedReview.getId());
                reviewListView.getItems().remove(selectedIndex);
            });
        } else {
            showWarningAlert("Aucune sélection", "Veuillez sélectionner un avis à supprimer.");
        }
    }

    private void showConfirmationAlert(String title, String content, Runnable onConfirmed) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText(title);
        alert.setContentText(content);
        alert.showAndWait().ifPresent(response -> {
            if (response == javafx.scene.control.ButtonType.OK) {
                onConfirmed.run();
            }
        });
    }

    private void showWarningAlert(String title, String content) {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(content);
        alert.showAndWait();
    }

    public void compte(MouseEvent mouseEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Compte.fxml"));
            Parent root = loader.load();
            Scene currentScene = ((Node) mouseEvent.getSource()).getScene();
            currentScene.setRoot(root);
        } catch (IOException e) {
        }
    }

    public void post(MouseEvent mouseEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/avis.fxml"));
            Parent root = loader.load();
            Scene currentScene = ((Node) mouseEvent.getSource()).getScene();
            currentScene.setRoot(root);
        } catch (IOException e) {
        }
    }

    public void logout(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Login.fxml"));
            Parent root = loader.load();
            Scene currentScene = ((Node) event.getSource()).getScene();
            currentScene.setRoot(root);
        } catch (IOException e) {
        }
    }
}
