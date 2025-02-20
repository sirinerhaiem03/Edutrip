package tn.esprit.tacheuser.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import tn.esprit.tacheuser.utils.UserSession;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Compte implements Initializable {
    @FXML
    Label nom;
    @FXML
    Label prenom;
    @FXML
    Label tel;
    @FXML
    Label mail;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        nom.setText(UserSession.getNom());
        prenom.setText(UserSession.getPrenom());
        tel.setText(UserSession.getNumTel()+"");
        mail.setText(UserSession.getMail());
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

    public void Editer(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/EditCompte.fxml"));
            Parent root = loader.load();
            Scene currentScene = ((Node) event.getSource()).getScene();
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
