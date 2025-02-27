package tn.EduTrip.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Priority;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import tn.EduTrip.entites.Vol;
import tn.EduTrip.services.ServiceVol;

import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Random;
import java.util.ResourceBundle;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import tn.EduTrip.utils.AviationStackService;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AfficherVolController implements Initializable {
    private final Random random = new Random();

    @FXML
    private ListView<Vol> volsListView;
    @FXML
    private Button ajouterVolBtn;

    private final ServiceVol serviceVol = new ServiceVol();
    private final AviationStackService apiService = new AviationStackService(); // Service pour l'API AviationStack

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
            private final Button modifierBtn = new Button();
            private final Button supprimerBtn = new Button();
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

                // Ajouter les icônes aux boutons
                ImageView modifierIcon = new ImageView(new Image(getClass().getResource("/images/modif.png").toExternalForm()));
                modifierIcon.setFitHeight(24);
                modifierIcon.setFitWidth(24);
                modifierBtn.setGraphic(modifierIcon);
                modifierBtn.setStyle("-fx-background-color: transparent; -fx-cursor: hand;");

                ImageView supprimerIcon = new ImageView(new Image(getClass().getResource("/images/supprime.png").toExternalForm()));
                supprimerIcon.setFitHeight(24);
                supprimerIcon.setFitWidth(24);
                supprimerBtn.setGraphic(supprimerIcon);
                supprimerBtn.setStyle("-fx-background-color: transparent; -fx-cursor: hand;");

                // Layout setup
                infoBox.getChildren().addAll(numVolLabel, aeroportLabel, dateLabel, prixLabel, placesLabel);
                actionsBox.getChildren().addAll(modifierBtn, supprimerBtn);
                actionsBox.setAlignment(Pos.CENTER_RIGHT);

                HBox.setHgrow(infoBox, Priority.ALWAYS);
                cellBox.getChildren().addAll(infoBox, actionsBox);

                // Ajout des actions
                modifierBtn.setOnAction(event -> modifierVol(getItem()));
                supprimerBtn.setOnAction(event -> supprimerVol(getItem()));
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

            // Récupération des données de l'API
            JSONArray apiResponse = apiService.getFlights();

            if (apiResponse == null || apiResponse.isEmpty()) {
                afficherAlerte("Information", "Aucun vol disponible via l'API pour le moment");
                return;
            }

            for (int i = 0; i < apiResponse.length(); i++) {
                JSONObject flight = apiResponse.getJSONObject(i);
                try {
                    JSONObject departure = flight.getJSONObject("departure");
                    JSONObject arrival = flight.getJSONObject("arrival");
                    JSONObject flightInfo = flight.getJSONObject("flight");

                    // Gestion des dates avec format alternatif
                    String dateDepartStr = departure.optString("estimated", "");
                    String dateArriveeStr = arrival.optString("estimated", "");

                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX");
                    Date dateDepart = !dateDepartStr.isEmpty() ? sdf.parse(dateDepartStr) : new Date();
                    Date dateArrivee = !dateArriveeStr.isEmpty() ? sdf.parse(dateArriveeStr) : new Date();

                    // Récupération des informations du vol
                    String numVol = flightInfo.optString("iata", "N/A");
                    String aeroportDepart = departure.optString("airport", "Inconnu");
                    String aeroportArrivee = arrival.optString("airport", "Inconnu");

                    Random random = new Random();
                    int placesDisponibles = flight.optInt("available_seats", 100);
                    double prixVol = flight.optDouble("price", 200.0);
                    Vol vol = new Vol(
                            0,
                            numVol,
                            placesDisponibles, // Places aléatoires
                            aeroportDepart,
                            aeroportArrivee,
                            new Timestamp(dateDepart.getTime()),
                            new Timestamp(dateArrivee.getTime()),
                            prixVol // Prix aléatoire
                    );

                    volsListView.getItems().add(vol);
                } catch (JSONException e) {
                    System.err.println("Erreur de structure JSON pour le vol " + i);
                    e.printStackTrace();
                } catch (Exception e) {
                    System.err.println("Erreur de traitement du vol " + i);
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            afficherAlerte("Erreur BDD", "Erreur de chargement depuis la base de données: " + e.getMessage());
            e.printStackTrace();
        } catch (JSONException e) {
            afficherAlerte("Erreur API", "Réponse API malformée: " + e.getMessage());
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
