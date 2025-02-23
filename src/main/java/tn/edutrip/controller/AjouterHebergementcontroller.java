package tn.edutrip.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import tn.edutrip.entities.Hebergement;
import tn.edutrip.services.ServiceHebergement;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AjouterHebergementcontroller implements Initializable {

    @FXML
    private TextField nomh;

    @FXML
    private TextField capaciteh;

    @FXML
    private TextField adressh;

    @FXML
    private TextField prixh;

    @FXML
    private ChoiceBox<String> disponibleh;

    @FXML
    private TextArea descriptionh;

    @FXML
    private ChoiceBox<String> typeh;

    @FXML
    private TextField imageh;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        disponibleh.getItems().addAll("Disponible", "Non disponible", "Réservée");
        disponibleh.setValue("Disponible");

        typeh.getItems().addAll(
                "Résidences universitaires publiques",
                "Résidences étudiantes privées",
                "Appartements",
                "Foyers"
        );
        typeh.setValue("Appartements");
    }

    @FXML
    void AjouterHebergement(ActionEvent event) {
        ServiceHebergement serviceHebergement = new ServiceHebergement();

        try {
            int capacite = Integer.parseInt(capaciteh.getText());
            float prix = Float.parseFloat(prixh.getText());

            if (capacite > 1000) {
                showAlert(Alert.AlertType.WARNING, "Capacité invalide", "La capacité ne doit pas dépasser 1000 !");
                return;
            }

            if (prix > 9000) {
                showAlert(Alert.AlertType.WARNING, "Prix invalide", "Le prix ne doit pas dépasser 9000 !");
                return;
            }

            String nom = nomh.getText().trim();
            String adresse = adressh.getText().trim();
            String disponibilite = disponibleh.getValue();
            String description = descriptionh.getText().trim();
            String type = typeh.getValue();
            String image = imageh.getText().trim();

            if (nom.isEmpty() || adresse.isEmpty() || description.isEmpty() || image.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Champs manquants", "Veuillez remplir tous les champs !");
                return;
            }

            Hebergement hebergement = new Hebergement(
                    0, nom, capacite, type, adresse, disponibilite, description, image, prix
            );

            serviceHebergement.add(hebergement);
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Hébergement ajouté avec succès !");

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherHebergement.fxml"));
            Parent parent = loader.load();
            AfficherHebergementController controller = loader.getController();
            controller.initialize();
            nomh.getScene().setRoot(parent);

        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur de format", "Veuillez entrer des valeurs numériques valides pour la capacité et le prix !");
        } catch (IOException e) {
            System.out.println("Erreur de chargement de l'interface: " + e.getMessage());
        }
    }

    @FXML
    public void AfficherHebergement(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/AfficherHebergement.fxml"));
            nomh.getScene().setRoot(root);
        } catch (IOException e) {
            System.out.println("Erreur lors du chargement de l'interface : " + e.getMessage());
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.show();
    }
}
