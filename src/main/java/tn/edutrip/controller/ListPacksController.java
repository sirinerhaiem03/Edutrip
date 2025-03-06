package tn.edutrip.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import tn.edutrip.entities.Pack_agence;
import tn.edutrip.services.ServicePack_agence;
import javafx.scene.input.KeyEvent;
import javafx.util.Duration;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

public class ListPacksController {

    @FXML
    private ListView<Pack_agence> listDesPacks;

    @FXML
    private TextField nompackrecherche; // Champ de recherche

    @FXML
    private ComboBox<String> combotripack; // ComboBox pour trier les packs

    private ServicePack_agence servicePack = new ServicePack_agence();

    private ObservableList<Pack_agence> packList;

    @FXML
    public void initialize() {
        loadData();
        initializeComboBox();
        setupSearch();
    }

    public void loadData() {
        List<Pack_agence> packs = servicePack.getAllPacks();
        packList = FXCollections.observableArrayList(packs);
        listDesPacks.setItems(packList);

        listDesPacks.setCellFactory(param -> new ListCell<Pack_agence>() {
            @Override
            protected void updateItem(Pack_agence pack, boolean empty) {
                super.updateItem(pack, empty);
                if (empty || pack == null) {
                    setGraphic(null);
                } else {
                    VBox vboxInfo = new VBox(
                            new Label("Nom: " + pack.getNomPk()),
                            new Label("Description: " + pack.getDescriptionPk()),
                            new Label("Prix: " + pack.getPrix() + "DT"),
                            new Label("Durée: " + pack.getDuree() + " jours"),
                            new Label("Service inclus: " + pack.getServices_inclus()),
                            new Label("Date Dajout: " + pack.getDate_ajout()),
                            new Label("Status: " + pack.getStatus())
                    );

                    Button updateButton = new Button("Mettre à jour");
                    Button deleteButton = new Button("Supprimer");
                    Button translateButton = new Button("Traduire en Anglais");

                    updateButton.setPrefWidth(120);
                    deleteButton.setPrefWidth(120);
                    translateButton.setPrefWidth(120);

                    updateButton.setStyle("-fx-background-color: #007bff; -fx-text-fill: white; -fx-padding: 5px;");
                    deleteButton.setStyle("-fx-background-color: #d9534f; -fx-text-fill: white; -fx-padding: 5px;");
                    translateButton.setStyle("-fx-background-color: #5bc0de; -fx-text-fill: white; -fx-padding: 5px;");

                    updateButton.setOnAction(event -> updateButton(event, pack));
                    deleteButton.setOnAction(event -> deletePack(event, pack));


                    VBox vboxButtons = new VBox(5, updateButton, deleteButton, translateButton);
                    vboxButtons.setStyle("-fx-alignment: center;");

                    HBox hboxContent = new HBox(10, vboxInfo, vboxButtons);
                    hboxContent.setStyle("-fx-alignment: center-left; -fx-padding: 10px;");

                    setGraphic(hboxContent);
                }
            }
        });
    }

    @FXML
    private void updateButton(ActionEvent event, Pack_agence pack) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/UpdatePack.fxml"));
            Parent root = loader.load();

            UpdatePackController updateController = loader.getController();
            updateController.loadPack(pack);

            Stage stage = new Stage();
            stage.setTitle("Mettre à jour le pack");
            stage.setScene(new Scene(root));

            stage.setOnHidden(e -> loadData());

            stage.show();
        } catch (IOException e) {
            showAlert("Erreur", "Impossible d'ouvrir la fenêtre de mise à jour.", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    @FXML
    private void deletePack(ActionEvent event, Pack_agence pack) {
        boolean isDeleted = servicePack.remove(pack.getId_pack());
        if (isDeleted) {
            loadData();
            showAlert("Succès", "Le pack a été supprimé avec succès.", Alert.AlertType.INFORMATION);
        } else {
            showAlert("Erreur", "Une erreur est survenue lors de la suppression du pack.", Alert.AlertType.ERROR);
        }
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Fonctionnalité de recherche
    private void setupSearch() {
        nompackrecherche.textProperty().addListener((observable, oldValue, newValue) -> {
            String filterText = newValue.toLowerCase();
            ObservableList<Pack_agence> filteredList = packList.stream()
                    .filter(pack -> pack.getNomPk().toLowerCase().contains(filterText))
                    .collect(Collectors.toCollection(FXCollections::observableArrayList));

            listDesPacks.setItems(filteredList);
        });
    }

    // Initialisation de la ComboBox pour le tri
    private void initializeComboBox() {
        combotripack.getItems().addAll("Nom", "Prix", "Durée");
        combotripack.setOnAction(this::trierAgences);
    }

    // Fonctionnalité de tri
    @FXML
    private void trierAgences(ActionEvent event) {
        String selectedSort = combotripack.getValue();
        if (selectedSort == null) return;

        switch (selectedSort) {
            case "Nom":
                listDesPacks.getItems().sort((pack1, pack2) -> pack1.getNomPk().compareToIgnoreCase(pack2.getNomPk()));
                break;
            case "Prix":
                listDesPacks.getItems().sort((pack1, pack2) -> pack1.getPrix().compareTo(pack2.getPrix()));
                break;
            case "Durée":
                listDesPacks.getItems().sort((pack1, pack2) -> Integer.compare(pack1.getDuree(), pack2.getDuree()));
                break;
            default:
                break;
        }
    }


}
