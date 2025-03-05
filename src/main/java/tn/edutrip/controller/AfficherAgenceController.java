package tn.edutrip.controller;

import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import tn.edutrip.entities.Agence;
import tn.edutrip.entities.Pack_agence;
import tn.edutrip.services.ServiceAgence;
import tn.edutrip.services.ServicePack_agence;

import java.util.List;

public class AfficherAgenceController {

    @FXML
    private ListView<Agence> listDesAgences;

    private ServicePack_agence servicePack = new ServicePack_agence();

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
            private VBox packsBox;

            {
                nameLabel = new Label();
                nameLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
                addressLabel = new Label();
                phoneLabel = new Label();
                emailLabel = new Label();
                packsBox = new VBox();

                VBox vbox = new VBox(nameLabel, addressLabel, phoneLabel, emailLabel, packsBox);
                content = new HBox(vbox);
                content.setSpacing(10);
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

                    packsBox.getChildren().clear(); // Clear previous pack details

                    List<Pack_agence> packs = servicePack.getPacksByAgence(agence.getIdAgence());
                    if (!packs.isEmpty()) {
                        Label packTitle = new Label("Packs disponibles:");
                        packTitle.setStyle("-fx-font-weight: bold;");
                        packsBox.getChildren().add(packTitle);

                        for (Pack_agence pack : packs) {
                            VBox packDetails = new VBox();
                            packDetails.getChildren().add(new Label("• " + pack.getNomPk() + " - " + pack.getPrix() + "DT"));
                            packDetails.getChildren().add(new Label("  Description: " + pack.getDescriptionPk()));
                            packDetails.getChildren().add(new Label("  Duration: " + pack.getDuree() + " days"));
                            packDetails.getChildren().add(new Label("  Included Services: " + pack.getServices_inclus()));
                            packDetails.getChildren().add(new Label("  Status: " + pack.getStatus()));
                            packDetails.getChildren().add(new Label("  Date Added: " + pack.getDate_ajout()));

                            packsBox.getChildren().add(packDetails);
                        }
                    } else {
                        packsBox.getChildren().add(new Label("Aucun pack disponible."));
                    }

                    setGraphic(content);
                }
            }
        });
    }
}
