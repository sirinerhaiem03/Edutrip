package tn.EduTrip.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Priority;
import tn.EduTrip.entites.Vol;
import tn.EduTrip.services.ServiceVol;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.geometry.Pos;
import javafx.scene.text.Text;

public class AfficherVolController implements Initializable {

    @FXML
    private ListView<Vol> volsListView;
    @FXML
    private Button ajouterVolBtn;

    private final ServiceVol serviceVol = new ServiceVol();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            setupListView();
            chargerVols();
        } catch (Exception e) {
            afficherAlerte("Erreur d'initialisation", "Une erreur est survenue lors de l'initialisation : " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void setupListView() {
        volsListView.setCellFactory(listView -> new ListCell<Vol>() {
            private final HBox cellBox = new HBox(20);
            private final VBox infoBox = new VBox(8);
            private final Label numVolLabel = new Label();
            private final Label aeroportLabel = new Label();
            private final Label dateLabel = new Label();
            private final Label prixLabel = new Label();
            private final Label placesLabel = new Label();
            private final Button modifierBtn = new Button("Modifier");
            private final Button supprimerBtn = new Button("Supprimer");
            private final HBox actionsBox = new HBox(10);

            {
                // Cell styling
                cellBox.setStyle("-fx-padding: 15; -fx-background-color: white; -fx-border-color: #e0e0e0; " +
                               "-fx-border-radius: 5; -fx-background-radius: 5;");
                cellBox.setAlignment(Pos.CENTER_LEFT);
                

                numVolLabel.setStyle("-fx-font-size: 16; -fx-font-weight: bold;");
                aeroportLabel.setStyle("-fx-font-size: 14;");
                dateLabel.setStyle("-fx-font-size: 12; -fx-text-fill: #666;");
                prixLabel.setStyle("-fx-font-size: 14; -fx-font-weight: bold; -fx-text-fill: #1a237e;");
                placesLabel.setStyle("-fx-font-size: 12; -fx-text-fill: #4CAF50;");

                // Buttons styling
                modifierBtn.setStyle("-fx-background-color: #1a237e; -fx-text-fill: white; " +
                                   "-fx-font-size: 14; -fx-padding: 8 15; -fx-background-radius: 5;");
                supprimerBtn.setStyle("-fx-background-color: #f44336; -fx-text-fill: white; " +
                                    "-fx-font-size: 14; -fx-padding: 8 15; -fx-background-radius: 5;");

                // Layout setup
                infoBox.getChildren().addAll(numVolLabel, aeroportLabel, dateLabel, prixLabel, placesLabel);
                actionsBox.getChildren().addAll(modifierBtn, supprimerBtn);
                actionsBox.setAlignment(Pos.CENTER_RIGHT);
                
                HBox.setHgrow(infoBox, Priority.ALWAYS);
                cellBox.getChildren().addAll(infoBox, actionsBox);

                // Hover effects
                modifierBtn.setOnMouseEntered(e -> modifierBtn.setStyle("-fx-background-color: #283593; -fx-text-fill: white; " +
                                                                       "-fx-font-size: 14; -fx-padding: 8 15; -fx-background-radius: 5;"));
                modifierBtn.setOnMouseExited(e -> modifierBtn.setStyle("-fx-background-color: #1a237e; -fx-text-fill: white; " +
                                                                      "-fx-font-size: 14; -fx-padding: 8 15; -fx-background-radius: 5;"));
                
                supprimerBtn.setOnMouseEntered(e -> supprimerBtn.setStyle("-fx-background-color: #d32f2f; -fx-text-fill: white; " +
                                                                         "-fx-font-size: 14; -fx-padding: 8 15; -fx-background-radius: 5;"));
                supprimerBtn.setOnMouseExited(e -> supprimerBtn.setStyle("-fx-background-color: #f44336; -fx-text-fill: white; " +
                                                                        "-fx-font-size: 14; -fx-padding: 8 15; -fx-background-radius: 5;"));
            }

            @Override
            protected void updateItem(Vol vol, boolean empty) {
                super.updateItem(vol, empty);

                if (empty || vol == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    numVolLabel.setText("Vol " + vol.getNumVol());
                    aeroportLabel.setText(vol.getDepart() + " ➜ " + vol.getArrivee());
                    dateLabel.setText("Départ : " + vol.getDateDepart().toLocalDateTime().format(
                            java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) + 
                            " | Arrivée : " + vol.getDateArrivee().toLocalDateTime().format(
                            java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
                    prixLabel.setText(String.format("Prix : %.2f TND", vol.getPrix()));
                    placesLabel.setText("Places disponibles : " + vol.getPlaces());

                    modifierBtn.setOnAction(event -> modifierVol(vol));
                    supprimerBtn.setOnAction(event -> supprimerVol(vol));

                    setText(null);
                    setGraphic(cellBox);
                }
            }
        });
    }

    private void modifierVol(Vol vol) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierVol.fxml"));
            Parent root = loader.load();
            
            ModifierVolController modifierController = loader.getController();
            modifierController.setVol(vol);
            
            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Modifier Vol");
            stage.show();
            
            // Refresh list after window closes
            stage.setOnHidden(e -> chargerVols());
        } catch (IOException e) {
            afficherAlerte("Erreur", "Impossible d'ouvrir la page de modification : " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void supprimerVol(Vol vol) {
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmation de suppression");
        confirmation.setHeaderText("Supprimer le vol " + vol.getNumVol() + " ?");
        confirmation.setContentText("Êtes-vous sûr de vouloir supprimer ce vol ?");

        confirmation.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    serviceVol.supprimer(vol.getId());
                    afficherAlerte("Succès", "Vol supprimé avec succès !");
                    chargerVols();
                } catch (SQLException e) {
                    afficherAlerte("Erreur", "Erreur lors de la suppression : " + e.getMessage());
                    e.printStackTrace();
                }
            }
        });
    }

    @FXML
    private void handleAjouterVol(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ajoutervol.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            afficherAlerte("Erreur", "Impossible d'ouvrir la page d'ajout : " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void chargerVols() {
        try {
            volsListView.getItems().clear();
            volsListView.getItems().addAll(serviceVol.afficher());
        } catch (SQLException e) {
            afficherAlerte("Erreur", "Impossible de charger les vols : " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void afficherAlerte(String titre, String contenu) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(contenu);
        alert.showAndWait();
    }
}
