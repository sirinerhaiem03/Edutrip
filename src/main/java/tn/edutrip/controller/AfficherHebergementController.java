package tn.edutrip.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import tn.edutrip.entities.Hebergement;
import tn.edutrip.services.ServiceHebergement;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class AfficherHebergementController {

    @FXML
    private ListView<Hebergement> listViewHebergement;

    @FXML
    private TextField nomidh; // Add this field

    private ServiceHebergement serviceHebergement = new ServiceHebergement();
    private ObservableList<Hebergement> hebergementList;
    private FilteredList<Hebergement> filteredHebergementList; // Add this field

    @FXML
    public void initialize() {
        loadData();

        // Add a listener to the TextField for search functionality
        nomidh.textProperty().addListener((observable, oldValue, newValue) -> {
            filterHebergementList(newValue);
        });
    }

    private void loadData() {
        List<Hebergement> hebergements = serviceHebergement.getAll();
        hebergementList = FXCollections.observableArrayList(hebergements);
        filteredHebergementList = new FilteredList<>(hebergementList, p -> true); // Initialize filtered list

        listViewHebergement.setItems(filteredHebergementList); // Set the filtered list to the ListView

        listViewHebergement.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Hebergement hebergement, boolean empty) {
                super.updateItem(hebergement, empty);

                if (empty || hebergement == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    // Create UI components for each item
                    ImageView imageView = new ImageView();
                    imageView.setFitHeight(80);
                    imageView.setFitWidth(80);
                    imageView.setPreserveRatio(true);

                    String imagePath = "file:/C:/Users/maram/IdeaProjects/EDUTRIP3/src/main/resources/images/" + hebergement.getImageh();
                    try {
                        imageView.setImage(new Image(imagePath));
                    } catch (Exception e) {
                        imageView.setImage(new Image("file:/C:/Users/maram/IdeaProjects/EDUTRIP3/src/main/resources/images/default_hebergement.png"));
                    }

                    Label nameLabel = new Label(hebergement.getNomh());
                    nameLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: black;");

                    Label locationLabel = new Label("\uD83D\uDCCD " + hebergement.getAdressh());
                    locationLabel.setStyle("-fx-text-fill: #555;");

                    Label priceLabel = new Label("\uD83D\uDCB0 " + hebergement.getPrixh() + " TND");
                    priceLabel.setStyle("-fx-text-fill: #28a745; -fx-font-weight: bold;");

                    Button updateButton = new Button("Modifier");
                    updateButton.setStyle("-fx-background-color: #5bc0de; -fx-text-fill: white;");
                    updateButton.setOnAction(event -> handleUpdate(hebergement));

                    Button deleteButton = new Button("Supprimer");
                    deleteButton.setStyle("-fx-background-color: #d9534f; -fx-text-fill: white;");
                    deleteButton.setOnAction(event -> handleDelete(hebergement));

                    Button reserveButton = new Button("Réservation");
                    reserveButton.setStyle("-fx-background-color: #f0ad4e; -fx-text-fill: white;");
                    reserveButton.setOnAction(event -> handleReservation(hebergement));

                    Button detailsButton = new Button("Détails");
                    detailsButton.setStyle("-fx-background-color: #007bff; -fx-text-fill: white;");
                    detailsButton.setOnAction(event -> handleDetails(hebergement));

                    VBox textLayout = new VBox(5, nameLabel, locationLabel, priceLabel);
                    textLayout.setMinWidth(200);

                    HBox hBox = new HBox(15, imageView, textLayout, updateButton, deleteButton, reserveButton, detailsButton);
                    hBox.setStyle("-fx-padding: 10px; -fx-background-color: #f9f9f9; -fx-border-color: #ddd; -fx-border-radius: 5px;");

                    setGraphic(hBox);
                }
            }
        });
    }

    private void filterHebergementList(String searchText) {
        // Filter the list based on the search text
        filteredHebergementList.setPredicate(hebergement -> {
            if (searchText == null || searchText.isEmpty()) {
                return true; // Show all items if the search text is empty
            }

            // Compare the hebergement name with the search text (case-insensitive)
            String lowerCaseFilter = searchText.toLowerCase();
            return hebergement.getNomh().toLowerCase().contains(lowerCaseFilter);
        });
    }


    private void handleDelete(Hebergement hebergement) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de suppression");
        alert.setHeaderText("Supprimer Hébergement");
        alert.setContentText("Êtes-vous sûr de vouloir supprimer cet hébergement ?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            serviceHebergement.remove(hebergement.getId_hebergement());
            hebergementList.remove(hebergement);
            listViewHebergement.refresh();
        }
    }

    private void handleUpdate(Hebergement hebergement) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/UpdateHebergement.fxml"));
            Parent root = loader.load();

            //Récupère le contrôleur (Controller) lié au fichier UpdateHebergement.fxml
            UpdateHebergementController controller = loader.getController();
            controller.setHebergement(hebergement);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Modifier Hébergement");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleDetails(Hebergement hebergement) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/DetailsHebergement.fxml"));
            Parent root = loader.load();

            DetailsHebergementController controller = loader.getController();
            controller.setHebergement(hebergement);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Détails de l'Hébergement");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void refreshHebergementList() {
        loadData();
    }


    private void handleReservation(Hebergement hebergement) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterReservation.fxml"));
            Parent root = loader.load();

            AjouterReservationController controller = loader.getController();
            controller.setHebergement(hebergement);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Réserver Hébergement");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}