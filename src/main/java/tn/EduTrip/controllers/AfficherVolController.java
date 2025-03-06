package tn.EduTrip.controllers;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.TemporalAccessor;
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
import java.util.concurrent.ThreadLocalRandom;

public class AfficherVolController implements Initializable {
    private final Random random = new Random();

    @FXML
    private ListView<Vol> volsListView;
    @FXML private TextField departureFilter;
    @FXML private TextField arrivalFilter;
    @FXML private DatePicker dateFilter;
    @FXML
    private Button ajouterVolBtn;
    private final ObservableList<Vol> allVols = FXCollections.observableArrayList();
    private final FilteredList<Vol> filteredVols = new FilteredList<>(allVols);

    private final ServiceVol serviceVol = new ServiceVol();
    private final AviationStackService apiService = new AviationStackService();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            volsListView.setItems(filteredVols);
            setupListView();
            chargerVols();
        } catch (Exception e) {
            afficherAlerte("Erreur d'initialisation", "Erreur lors de l'initialisation : " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void appliquerFiltres(ActionEvent event) {
        updateFilters();
    }

    @FXML
    private void reinitialiserFiltres(ActionEvent event) {
        departureFilter.clear();
        arrivalFilter.clear();
        dateFilter.setValue(null);
        updateFilters();
    }

    private void updateFilters() {
        filteredVols.setPredicate(vol -> {
            String depart = departureFilter.getText().toLowerCase();
            String arrivee = arrivalFilter.getText().toLowerCase();
            LocalDate date = dateFilter.getValue();

            boolean matchDepart = depart.isEmpty() || vol.getDepart().toLowerCase().contains(depart);
            boolean matchArrivee = arrivee.isEmpty() || vol.getArrivee().toLowerCase().contains(arrivee);
            boolean matchDate = date == null || matchesDate(vol.getDateDepart(), date);

            return matchDepart && matchArrivee && matchDate;
        });
    }

    private boolean matchesDate(Timestamp timestamp, LocalDate date) {
        return timestamp.toLocalDateTime().toLocalDate().isEqual(date);
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
                cellBox.setStyle("-fx-padding: 15; -fx-background-color: white; -fx-border-color:  #57A0D2; " +
                        "-fx-border-radius: 5; -fx-background-radius: 5;");
                cellBox.setAlignment(Pos.CENTER_LEFT);

                numVolLabel.setStyle("-fx-font-size: 16; -fx-font-weight: bold; -fx-text-fill: #333;");
                aeroportLabel.setStyle("-fx-font-size: 14; -fx-text-fill: #444;");
                dateLabel.setStyle("-fx-font-size: 12; -fx-text-fill: #666;");
                prixLabel.setStyle("-fx-font-size: 14; -fx-font-weight: bold; -fx-text-fill: #1a237e;");
                placesLabel.setStyle("-fx-font-size: 12; -fx-text-fill: #4CAF50;");

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
                    cellBox.setStyle("-fx-background-color: white; -fx-border-color: #e0e0e0; " +
                        "-fx-border-radius: 5; -fx-background-radius: 5;");


                    numVolLabel.setText("Vol " + vol.getNumVol());
                    aeroportLabel.setText(vol.getDepart() + " ➜ " + vol.getArrivee());
                    dateLabel.setText("Départ : " + vol.getDateDepart().toLocalDateTime().format(
                            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) +
                            " | Arrivée : " + vol.getDateArrivee().toLocalDateTime().format(
                            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
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
                    serviceVol.supprimer(vol.getId_Vol());
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

    @FXML
    private void handlajouterPerturbation(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Perturbation.fxml"));
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
            allVols.clear();
            allVols.addAll(serviceVol.afficher());

            JSONArray apiResponse = apiService.getFlights();
            if (apiResponse == null || apiResponse.isEmpty()) {
                afficherAlerte("Information", "Aucun vol disponible via l'API");
                return;
            }

            for (int i = 0; i < apiResponse.length(); i++) {
                JSONObject flight = apiResponse.getJSONObject(i);
                try {
                    JSONObject departure = flight.getJSONObject("departure");
                    JSONObject arrival = flight.getJSONObject("arrival");

                    // Extraction correcte des aéroports
                    String aeroportDepart = parseAirport(departure.opt("airport"));
                    String aeroportArrivee = parseAirport(arrival.opt("airport"));

                    // Utilisation de 'scheduled' au lieu de 'estimated'
                    Date dateDepart = parseDate(departure.optString("scheduled", ""));
                    Date dateArrivee = parseDate(arrival.optString("scheduled", ""));

                    JSONObject flightInfo = flight.optJSONObject("flight");
                    String numVol = flightInfo != null ? flightInfo.optString("iata", "N/A") : "N/A";

                    int placesDisponibles = flight.optInt("available_seats", random.nextInt(50) + 50);
                    double prixVol = flight.optDouble("price", 150.0 + random.nextDouble() * 200);

                    Vol vol = new Vol(
                            0,
                            numVol,
                            placesDisponibles,
                            aeroportDepart,
                            aeroportArrivee,
                            new Timestamp(dateDepart.getTime()),
                            new Timestamp(dateArrivee.getTime()),
                            prixVol
                    );

                    allVols.add(vol);
                    System.out.println("Vol ajouté : " + vol); // Debug
                } catch (JSONException e) {
                    System.err.println("Erreur de traitement du vol " + i + ": " + e.getMessage());
                    e.printStackTrace();
                }
            }
            filteredVols.setPredicate(null); // Force la mise à jour
        } catch (SQLException e) {
            afficherAlerte("Erreur BDD", "Erreur de chargement : " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Méthodes utilitaires ajoutées
    private String parseAirport(Object airport) {
        if (airport instanceof JSONObject) {
            // Extraction du nom de l'aéroport depuis l'objet JSON
            return ((JSONObject) airport).optString("name", "Aéroport inconnu");
        } else if (airport instanceof String) {
            // Si c'est une chaîne, retourner directement
            return (String) airport;
        }
        return "Aéroport inconnu";
    }

    private Date parseDate(String dateStr) {
        if (dateStr == null || dateStr.isEmpty()) {
            return generateRandomRealisticDate();
        }

        // Nettoyer la chaîne de date
        dateStr = dateStr.replace("Z", "+00:00") // Gérer le format Zulu time
                .split("\\.")[0]; // Enlever les millisecondes

        // Ajouter tous les formats possibles
        DateTimeFormatter[] formatters = {
                DateTimeFormatter.ISO_OFFSET_DATE_TIME,
                DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX"),
                DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"),
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"),
                DateTimeFormatter.ofPattern("yyyy-MM-dd")
        };

        for (DateTimeFormatter formatter : formatters) {
            try {
                TemporalAccessor parsed = formatter.parse(dateStr);
                LocalDateTime ldt = LocalDateTime.from(parsed);
                return Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
            } catch (DateTimeParseException e) {
                // Continuer avec le formateur suivant
            }
        }

        System.err.println("Aucun format ne correspond pour : " + dateStr);
        return generateRandomRealisticDate();
    }


    private Date generateRandomRealisticDate() {
        // Générer entre maintenant et 6 mois avec des heures/minutes réalistes
        LocalDateTime baseDate = LocalDateTime.now()
                .plusMonths(ThreadLocalRandom.current().nextInt(0, 6))
                .plusDays(ThreadLocalRandom.current().nextInt(0, 30));

        // Heures de vol réalistes (06h-23h)
        int hour = ThreadLocalRandom.current().nextInt(6, 24);
        int minute = ThreadLocalRandom.current().nextInt(0, 60);

        LocalDateTime randomDate = baseDate
                .withHour(hour)
                .withMinute(minute)
                .withSecond(0);

        return Date.from(randomDate.atZone(ZoneId.systemDefault()).toInstant());
    }


    private void afficherAlerte(String titre, String contenu) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(contenu);
        alert.showAndWait();
    }
}
