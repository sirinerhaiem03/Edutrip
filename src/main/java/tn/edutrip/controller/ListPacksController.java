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

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class ListPacksController {

    @FXML
    private ListView<Pack_agence> listDesPacks;

    @FXML
    private TextField nompackrecherche;

    @FXML
    private ComboBox<String> combotripack;

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

        // Customize the ListCell to include Update and Delete buttons
        listDesPacks.setCellFactory(param -> new ListCell<Pack_agence>() {
            @Override
            protected void updateItem(Pack_agence pack, boolean empty) {
                super.updateItem(pack, empty);
                if (empty || pack == null) {
                    setGraphic(null);
                } else {
                    // Create buttons
                    Button updateButton = new Button("Update");
                    Button deleteButton = new Button("Delete");

                    // Set button actions
                    updateButton.setOnAction(event -> handleUpdatePack(pack));
                    deleteButton.setOnAction(event -> handleDeletePack(pack));

                    // Create a VBox to hold pack details and buttons
                    VBox vboxInfo = new VBox(
                            new Label("Nom: " + pack.getNomPk()),
                            new Label("Description: " + pack.getDescriptionPk()),
                            new Label("Prix: " + pack.getPrix() + "DT"),
                            new Label("Durée: " + pack.getDuree() + " jours"),
                            new Label("Service inclus: " + pack.getServices_inclus()),
                            new Label("Date Dajout: " + pack.getDate_ajout()),
                            new Label("Status: " + pack.getStatus()),
                            new HBox(updateButton, deleteButton) // Add buttons in an HBox
                    );

                    setGraphic(vboxInfo);
                }
            }
        });
    }

    private void setupSearch() {
        nompackrecherche.textProperty().addListener((observable, oldValue, newValue) -> {
            String filterText = newValue.toLowerCase();
            ObservableList<Pack_agence> filteredList = packList.stream()
                    .filter(pack -> pack.getNomPk().toLowerCase().contains(filterText))
                    .collect(Collectors.toCollection(FXCollections::observableArrayList));

            listDesPacks.setItems(filteredList);
        });
    }

    private void initializeComboBox() {
        combotripack.getItems().addAll("Nom", "Prix", "Durée");
        combotripack.setOnAction(this::trierAgences);
    }

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

    private void handleUpdatePack(Pack_agence pack) {
        try {
            // Load the UpdatePack.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/UpdatePack.fxml"));
            Parent root = loader.load();

            // Pass the selected pack to the UpdatePackController
            UpdatePackController updateController = loader.getController();
            updateController.loadPack(pack); // Use loadPack instead of setPack

            // Open the UpdatePack window
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Update Pack");
            stage.show();

            // Rafraîchir la liste après mise à jour
            stage.setOnHidden(e -> loadData());
        } catch (IOException e) {
            e.printStackTrace();
            showErrorAlert("Erreur lors de l'ouverture de la page de mise à jour.");
        }
    }

    private void handleDeletePack(Pack_agence pack) {
        try {


            // Remove the pack from the ObservableList
            packList.remove(pack);

            // Refresh the ListView
            listDesPacks.setItems(packList);

            showSuccessAlert("Pack supprimé avec succès !");
        } catch (Exception e) {
            e.printStackTrace();
            showErrorAlert("Erreur lors de la suppression du pack.");
        }
    }

    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showSuccessAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Succès");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}