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
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class AfficherHebergementController {

    @FXML
    private ListView<Hebergement> listViewHebergement;

    @FXML
    private TextField nomidh; // For search functionality

    @FXML
    private ChoiceBox<String> choicebox; // For sorting functionality

    private ServiceHebergement serviceHebergement = new ServiceHebergement();
    private ObservableList<Hebergement> hebergementList;
    private FilteredList<Hebergement> filteredHebergementList;

    @FXML
    public void initialize() {
        loadData();

        // Add a listener to the TextField for search functionality
        nomidh.textProperty().addListener((observable, oldValue, newValue) -> {
            filterHebergementList(newValue);
        });

        // Add a listener to the ChoiceBox for sorting functionality
        choicebox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            sortHebergementList(newValue);
        });

        // Initialize the ChoiceBox with sorting options
        choicebox.setItems(FXCollections.observableArrayList(
                "Trier par nom",
                "Trier par prix",
                "Trier par capacité"
        ));
        choicebox.setValue("Trier par nom"); // Default sorting option
    }

    private void loadData() {
        List<Hebergement> hebergements = serviceHebergement.getAll();
        hebergementList = FXCollections.observableArrayList(hebergements);
        filteredHebergementList = new FilteredList<>(hebergementList, p -> true);

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

                    String imagePath = "file:/C:/Users/maram/IdeaProjects/edutrip/src/main/resources/images/" + hebergement.getImageh();
                    try {
                        imageView.setImage(new Image(imagePath));
                    } catch (Exception e) {
                        imageView.setImage(new Image("file:/C:/Users/maram/IdeaProjects/edutrip/src/main/resources/images/default_hebergement.png"));
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
        if (searchText == null || searchText.isEmpty()) {
            filteredHebergementList.setPredicate(hebergement -> true); // Show all items if search is empty
        } else {
            String lowerCaseFilter = searchText.toLowerCase();

            // Use Stream API to filter the list
            List<Hebergement> filteredList = hebergementList.stream()
                    .filter(hebergement -> hebergement.getNomh().toLowerCase().contains(lowerCaseFilter))
                    .toList(); // Available in Java 16+, otherwise use .collect(Collectors.toList())

            // Update the filtered list
            filteredHebergementList.setPredicate(filteredList::contains);
        }
    }

    private void sortHebergementList(String sortOption) {
        Comparator<Hebergement> comparator = null;

        // Determine the comparator based on the selected sorting option
        switch (sortOption) {
            case "Trier par nom":
                comparator = Comparator.comparing(Hebergement::getNomh);
                break;
            case "Trier par prix":
                comparator = Comparator.comparingDouble(Hebergement::getPrixh);
                break;
            case "Trier par capacité":
                comparator = Comparator.comparingInt(Hebergement::getCapaciteh);
                break;
            default:
                comparator = Comparator.comparing(Hebergement::getNomh); // Default sorting
        }

        // Use Java Streams to sort the list
        List<Hebergement> sortedList = hebergementList.stream()
                .sorted(comparator)
                .toList(); // Collect the sorted list (Java 16+)

        // Update the ObservableList with the sorted data
        hebergementList.setAll(sortedList);
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