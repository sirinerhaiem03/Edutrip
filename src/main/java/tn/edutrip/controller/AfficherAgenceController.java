package tn.edutrip.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import tn.edutrip.entities.Agence;
import tn.edutrip.services.ServiceAgence;

import java.util.List;

public class AfficherAgenceController {

    @FXML
    private ListView<Agence> listDesAgences;

    @FXML
    public void initialize() {

        ServiceAgence serviceAgence = new ServiceAgence();


        List<Agence> agenceList = serviceAgence.afficher();


        listDesAgences.getItems().addAll(agenceList);


        listDesAgences.setCellFactory(param -> new ListCell<Agence>() {
            private HBox content;
            private Label nameLabel;
            private Label addressLabel;
            private Label phoneLabel;
            private Label emailLabel;
            private Button viewButton;

            {

                nameLabel = new Label();
                nameLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
                addressLabel = new Label();
                phoneLabel = new Label();
                emailLabel = new Label();

                viewButton = new Button("View Details");
                viewButton.setStyle("-fx-background-color: lightblue; -fx-text-fill: white;");


                VBox vbox = new VBox(nameLabel, addressLabel, phoneLabel, emailLabel, viewButton);
                content = new HBox(vbox);
                content.setSpacing(10);


                viewButton.setOnAction(event -> {
                    Agence agence = getItem();
                    if (agence != null) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Agence Details");
                        alert.setHeaderText(agence.getNomAg());
                        alert.setContentText("Address: " + agence.getAdresseAg() +
                                "\nPhone: " + agence.getTelephoneAg() +
                                "\nEmail: " + agence.getEmailAg() +
                                "\nDescription: " + agence.getDescriptionAg());
                        alert.showAndWait();
                    }
                });
            }

            @Override
            protected void updateItem(Agence agence, boolean empty) {
                super.updateItem(agence, empty);
                if (empty || agence == null) {
                    setGraphic(null);
                } else {

                    nameLabel.setText(agence.getNomAg());
                    addressLabel.setText("Address: " + agence.getAdresseAg());
                    phoneLabel.setText("Phone: " + agence.getTelephoneAg());
                    emailLabel.setText("Email: " + agence.getEmailAg());

                    setGraphic(content);
                }
            }

        });
    }
}
