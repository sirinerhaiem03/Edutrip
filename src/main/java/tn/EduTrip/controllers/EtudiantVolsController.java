package tn.EduTrip.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import tn.EduTrip.entites.Vol;
import tn.EduTrip.services.ServiceVol;
import tn.EduTrip.utils.AviationStackService;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import static org.apache.hc.client5.http.utils.DateUtils.parseDate;

public class EtudiantVolsController implements Initializable {
    private final AviationStackService apiService = new AviationStackService();
    @FXML
    private VBox volContainer;
    @FXML
    private TextField departureFilter, arrivalFilter;
    @FXML
    private DatePicker dateFilter;

    private final ServiceVol serviceVol = new ServiceVol();
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private final ObservableList<Vol> allVols = FXCollections.observableArrayList();
    private final FilteredList<Vol> filteredVols = new FilteredList<>(allVols);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            chargerVols();
            setupDynamicFiltering();
        } catch (Exception e) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur", "Erreur lors du chargement des vols: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private Vol convertJsonToVol(JSONObject flightJson) {
        try {
            JSONObject flightInfo = flightJson.getJSONObject("flight");
            JSONObject departure = flightJson.getJSONObject("departure");
            JSONObject arrival = flightJson.getJSONObject("arrival");
            String numVol = flightInfo.optString("iata", "VOL-" + new Random().nextInt(1000));
            String depart = parseAirport(departure);
            String arrivee = parseAirport(arrival);

            // Parsing des dates
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX");
            Date dateDepart = parseDate(departure.optString("scheduled"), sdf);
            Date dateArrivee = parseDate(arrival.optString("scheduled"), sdf);

            return new Vol(
                    numVol,
                    depart,
                    arrivee,
                    new Timestamp(dateDepart.getTime()),
                    new Timestamp(dateArrivee.getTime()),
                    flightJson.optDouble("price", 500 + (Math.random() * 1000)),
                    flightJson.optInt("available_seats", 50 + (int)(Math.random() * 150))
            );

        } catch (JSONException e) {
            System.err.println("Erreur de conversion - Données API brutes : " + flightJson.toString());
            return null;
        }
    }

    private String parseAirport(JSONObject location) {
    try {
        // Essayer de lire comme objet imbriqué
        return location.getJSONObject("airport").getString("name");
    } catch (JSONException e) {
        // Fallback : lire comme chaîne simple
        return location.optString("airport", "Aéroport inconnu");
    }
}
    private Date parseDate(String dateStr, SimpleDateFormat sdf) {
        try {
            return !dateStr.isEmpty() ? sdf.parse(dateStr) : new Date();
        } catch (Exception e) {
            System.err.println("Erreur de parsing de date, utilisation de la date actuelle");
            return new Date();
        }
    }
    private void chargerVols() {
        try {
            allVols.clear();
            JSONArray apiFlights = apiService.getFlights();
            if (apiFlights == null || apiFlights.isEmpty()) {
                afficherAlerte(Alert.AlertType.INFORMATION, "Information", "Aucun vol disponible");
                return;
            }
            for (int i = 0; i < apiFlights.length(); i++) {
                JSONObject flight = apiFlights.getJSONObject(i);
                Vol vol = convertJsonToVol(flight);
                if (vol != null) allVols.add(vol);
            }
            updateVolDisplay();
        } catch (JSONException e) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur API", e.getMessage());
        }catch (Exception e) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur", "Erreur générale: " + e.getMessage());
        }
    }

    private void updateVolDisplay() {
        volContainer.getChildren().clear();

        if (filteredVols.isEmpty()) {
            return; // Ne rien ajouter, donc pas d'espace vide
        }

        for (Vol vol : filteredVols) {
            volContainer.getChildren().add(creerVolItem(vol));
        }
    }



    private HBox creerVolItem(Vol vol) {
        HBox cellBox = new HBox(20);
        cellBox.setStyle("-fx-padding: 15; -fx-background-color: white; -fx-border-color: #e0e0e0; -fx-border-width: 1; -fx-border-radius: 5; -fx-background-radius: 5; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 0);");

        VBox infoBox = new VBox(10);
        HBox.setHgrow(infoBox, Priority.ALWAYS);

        Label numVolLabel = new Label("✈ Vol " + vol.getNumVol());
        numVolLabel.setFont(Font.font("System", FontWeight.BOLD, 18));
        numVolLabel.setStyle("-fx-text-fill: #1a237e;");

        Label routeLabel = new Label(vol.getDepart() + " → " + vol.getArrivee());
        Label departLabel = new Label("Départ: " + vol.getDateDepart().toLocalDateTime().format(dateFormatter));
        Label arrivalLabel = new Label("Arrivée: " + vol.getDateArrivee().toLocalDateTime().format(dateFormatter));
        Label prixLabel = new Label(String.format("Prix: %.2f TND", vol.getPrix()));
        Label placesLabel = new Label("Places disponibles: " + vol.getPlaces());

        prixLabel.setStyle("-fx-text-fill: #2e7d32; -fx-font-weight: bold;");
        placesLabel.setStyle("-fx-text-fill: #1976d2;");

        infoBox.getChildren().addAll(numVolLabel, routeLabel, departLabel, arrivalLabel, prixLabel, placesLabel);

        Button reserverBtn = new Button("Réserver");
        reserverBtn.setStyle("-fx-background-color: #2370c3; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10 20; -fx-background-radius: 5;");
        reserverBtn.setDisable(vol.getPlaces() <= 0);
        reserverBtn.setOnAction(event -> reserverVol(vol));

        cellBox.getChildren().addAll(infoBox, reserverBtn);
        return cellBox;
    }

    private void reserverVol(Vol vol) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ReservationVol.fxml"));
            Parent root = loader.load();

            ReserverVolsController controller = loader.getController();
            controller.setVol(vol);

            Stage stage = new Stage();
            stage.setTitle("Réserver un Vol");
            stage.setScene(new Scene(root));
            stage.setOnHidden(e -> chargerVols());
            stage.show();
        } catch (IOException e) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur", "Impossible d'ouvrir la page de réservation : " + e.getMessage());
        }
    }

    private void setupDynamicFiltering() {
        departureFilter.textProperty().addListener((obs, oldVal, newVal) -> updateFilters());
        arrivalFilter.textProperty().addListener((obs, oldVal, newVal) -> updateFilters());
        dateFilter.valueProperty().addListener((obs, oldVal, newVal) -> updateFilters());
    }

    private void updateFilters() {
        String depart = departureFilter.getText().toLowerCase();
        String arrivee = arrivalFilter.getText().toLowerCase();
        LocalDate date = dateFilter.getValue();
        List<Vol> volsFiltres = allVols.stream()
                .filter(vol -> depart.isEmpty() || vol.getDepart().toLowerCase().contains(depart))
                .filter(vol -> arrivee.isEmpty() || vol.getArrivee().toLowerCase().contains(arrivee))
                .filter(vol -> date == null || vol.getDateDepart().toLocalDateTime().toLocalDate().equals(date))
                .collect(Collectors.toList());

        afficherVolsFiltres(volsFiltres);
    }
    private void afficherVolsFiltres(List<Vol> volsFiltres) {
        volContainer.getChildren().clear();

        if (volsFiltres.isEmpty()) {
            return;
        }

        for (Vol vol : volsFiltres) {
            volContainer.getChildren().add(creerVolItem(vol));
        }
    }
    @FXML
    private void appliquerFiltres() {
        updateFilters();
    }

    @FXML
    private void reinitialiserFiltres() {
        departureFilter.clear();
        arrivalFilter.clear();
        dateFilter.setValue(null);
        updateFilters();
    }

    private void afficherAlerte(Alert.AlertType type, String titre, String contenu) {
        Alert alert = new Alert(type);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(contenu);
        alert.showAndWait();
    }
}
