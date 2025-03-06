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
import tn.esprit.tacheuser.utils.UserSession;

import java.io.IOException;
import java.util.List;

public class AdminController {

    @FXML
    private ListView<String> userListView;
    @FXML
    private ListView<String> reviewListView;
    @FXML
    private Button deleteUserButton;
    AvisService MRC=new AvisService();

    private final UserService userService;
    private List<User> users; // Stocker la liste des utilisateurs

    public AdminController() {
        this.userService = new UserService();
    }

    @FXML
    public void initialize() {
        for (Avis A : MRC.fetch()) {
            System.out.println("fez,opfaze,f "+  A.getUserId());
            reviewListView.getItems().add( A.getCommentaire() + " " + " - Note: " + A.getNote());
        }loadUsers();


        // Associer la suppression d'un utilisateur au bouton
        deleteUserButton.setOnAction(event -> deleteUser());
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

            // Confirmer la suppression
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText("Suppression d'utilisateur");
            alert.setContentText("Voulez-vous vraiment supprimer " + selectedUser.getNom() + " ?");
            alert.showAndWait().ifPresent(response -> {
                if (response == javafx.scene.control.ButtonType.OK) {
                    userService.deleteUser(selectedUser.getId());
                    loadUsers(); // Rafraîchir la liste après suppression
                }
            });
        } else {
            // Afficher un message si aucun utilisateur n'est sélectionné
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Aucune sélection");
            alert.setHeaderText("Aucun utilisateur sélectionné");
            alert.setContentText("Veuillez sélectionner un utilisateur à supprimer.");
            alert.showAndWait();
        }
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
