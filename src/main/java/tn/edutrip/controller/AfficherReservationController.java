package tn.edutrip.controller;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.client.j2se.MatrixToImageWriter;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import tn.edutrip.entities.Hebergement;
import tn.edutrip.entities.Reservation_hebergement;
import tn.edutrip.services.ServiceHebergement;
import tn.edutrip.services.ServiceReservationHebergement;



import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class AfficherReservationController {
    @FXML
    private ListView<Reservation_hebergement> listViewReservation;

    private final ServiceReservationHebergement serviceReservation = new ServiceReservationHebergement();
    private final ServiceHebergement serviceHebergement = new ServiceHebergement();
    private ObservableList<Reservation_hebergement> reservationList;

    @FXML
    private Button btnGeneratePDF;

    @FXML
    public void initialize() {
        loadData();

        btnGeneratePDF.setOnAction(event -> {
            Reservation_hebergement selectedReservation = listViewReservation.getSelectionModel().getSelectedItem();
            if (selectedReservation != null) {
                generatePDF(selectedReservation);
            } else {
                showAlert("Sélectionnez une réservation d'abord.");
            }
        });
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Avertissement");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void loadData() {
        List<Reservation_hebergement> reservations = serviceReservation.getAll();
        reservationList = FXCollections.observableArrayList(reservations);
        listViewReservation.setItems(reservationList);

        listViewReservation.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Reservation_hebergement reservation, boolean empty) {
                super.updateItem(reservation, empty);

                if (empty || reservation == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    // Labels for reservation details
                    Label dateLabel = new Label("\uD83D\uDCC5 Du " + reservation.getDate_d() + " au " + reservation.getDate_f());
                    dateLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: black;");

                    Label statusLabel = new Label("\uD83D\uDD12 Commentaire: " + reservation.getStatus());
                    statusLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: black;");

                    // Set a fixed width for the VBox containing text
                    VBox textLayout = new VBox(5, dateLabel, statusLabel);
                    textLayout.setMinWidth(250); // Adjust width as needed
                    textLayout.setMaxWidth(250);

                    // Buttons for modifying and deleting
                    Button updateButton = new Button("Modifier");
                    updateButton.setStyle("-fx-background-color: #5bc0de; -fx-text-fill: white;");
                    updateButton.setOnAction(event -> handleUpdate(reservation));

                    Button deleteButton = new Button("Supprimer");
                    deleteButton.setStyle("-fx-background-color: #d9534f; -fx-text-fill: white;");
                    deleteButton.setOnAction(event -> handleDelete(reservation));

                    // Spacer to push buttons to the right
                    Region spacer = new Region();
                    HBox.setHgrow(spacer, javafx.scene.layout.Priority.ALWAYS);

                    // HBox with spacing and styling
                    HBox hBox = new HBox(10, textLayout, spacer, updateButton, deleteButton);
                    hBox.setStyle("-fx-padding: 10px; -fx-background-color: #f9f9f9; -fx-border-color: #ddd; -fx-border-radius: 5px;");
                    hBox.setMinWidth(500);  // Ensure uniform width for all cells
                    hBox.setMaxWidth(500);

                    setGraphic(hBox);

                    // Ensure text color remains black even when selected
                    selectedProperty().addListener((obs, wasSelected, isSelected) -> {
                        if (isSelected) {
                            dateLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: black;");
                            statusLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: black;");
                        }
                    });
                }
            }
        });
    }


    public void generatePDF(Reservation_hebergement reservation) {
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                float pageWidth = page.getMediaBox().getWidth();
                float pageHeight = page.getMediaBox().getHeight();

                // Couleurs
                Color themeBlue = new Color(87, 160, 210);
                Color backgroundColor = new Color(226, 240, 254);

                // Fond du document
                contentStream.setNonStrokingColor(backgroundColor);
                contentStream.addRect(0, 0, pageWidth, pageHeight);
                contentStream.fill();

                // En-tête
                float headerHeight = 70;
                contentStream.setNonStrokingColor(themeBlue);
                contentStream.addRect(0, pageHeight - headerHeight, pageWidth, headerHeight);
                contentStream.fill();

                // Logo
                String logoPath = "C:/Users/maram/IdeaProjects/edutrip/src/main/resources/images/logo.png";
                File logoFile = new File(logoPath);
                if (logoFile.exists()) {
                    PDImageXObject logo = PDImageXObject.createFromFile(logoPath, document);
                    contentStream.drawImage(logo, 20, pageHeight - 60, 120, 50);
                }

                // Titre
                contentStream.setNonStrokingColor(Color.WHITE);
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 18);
                contentStream.beginText();
                contentStream.newLineAtOffset((pageWidth / 2) - 80, pageHeight - 45);
                contentStream.showText("Facture de la Réservation");
                contentStream.endText();

                float yPosition = pageHeight - 140;

                // Récupération des détails de l'hébergement
                Hebergement hebergement = serviceHebergement.getHebergementById(reservation.getId_hebergement());

                // Image Hébergement
                if (hebergement != null && hebergement.getImageh() != null) {
                    String imagePath = "C:/Users/maram/IdeaProjects/edutrip/src/main/resources/images/" + hebergement.getImageh();
                    File imageFile = new File(imagePath);

                    if (imageFile.exists()) {
                        PDImageXObject image = PDImageXObject.createFromFile(imagePath, document);
                        float imageWidth = 250;
                        float imageHeight = 180;
                        contentStream.drawImage(image, (pageWidth - imageWidth) / 2, yPosition - imageHeight, imageWidth, imageHeight);
                        yPosition -= (imageHeight + 20);
                    } else {
                        contentStream.setFont(PDType1Font.HELVETICA, 12);
                        contentStream.setNonStrokingColor(Color.RED);
                        contentStream.beginText();
                        contentStream.newLineAtOffset((pageWidth - 100) / 2, yPosition - 20);
                        contentStream.showText("Image non trouvée");
                        contentStream.endText();
                        yPosition -= 30;
                    }
                }

                // Ligne de séparation
                contentStream.setStrokingColor(themeBlue);
                contentStream.moveTo(50, yPosition);
                contentStream.lineTo(pageWidth - 50, yPosition);
                contentStream.stroke();
                yPosition -= 20;

                // Format des dates
                String dateD = formatDate(reservation.getDate_d());
                String dateF = formatDate(reservation.getDate_f());

                // Détails de la réservation
                float textXPosition = pageWidth / 2 - 100;
                addStyledText(contentStream, "Date de début:", dateD, textXPosition, yPosition, themeBlue);
                yPosition -= 20;
                addStyledText(contentStream, "Date de fin:", dateF, textXPosition, yPosition, themeBlue);
                yPosition -= 20;
                addStyledText(contentStream, "Hébergement:", (hebergement != null && hebergement.getNomh() != null ? hebergement.getNomh() : "Non trouvé"), textXPosition, yPosition, themeBlue);
                yPosition -= 20;
                addStyledText(contentStream, "Prix:", (hebergement != null ? hebergement.getPrixh() + " TND" : "Non disponible"), textXPosition, yPosition, themeBlue);
                yPosition -= 20;
                addStyledText(contentStream, "Commentaire:", reservation.getStatus(), textXPosition, yPosition, themeBlue);
                yPosition -= 30;

                // Génération du QR Code
                String qrData = "Réservation ID: " + reservation.getId_reservationh() +
                        "\nHébergement: " + (hebergement != null ? hebergement.getNomh() : "N/A") +
                        "\nPrix par nuit: " + (hebergement != null ? hebergement.getPrixh() : "N/A") + " TND" +
                        "\nDate début: " + dateD +
                        "\nDate fin: " + dateF;

                String qrDirectory = "C:/Users/maram/IdeaProjects/edutrip/src/main/resources/qrcodes/";
                new File(qrDirectory).mkdirs();
                String qrPath = qrDirectory + "reservation_" + reservation.getId_reservationh() + ".png";
                generateQRCode(qrData, qrPath);

                // Ajout du QR Code
                PDImageXObject qrImage = PDImageXObject.createFromFile(qrPath, document);
                contentStream.drawImage(qrImage, pageWidth - 150, 100, 100, 100);

                // Footer
                float footerHeight = 70;
                contentStream.setNonStrokingColor(themeBlue);
                contentStream.addRect(0, 0, pageWidth, footerHeight);
                contentStream.fill();

                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 10);
                contentStream.setNonStrokingColor(java.awt.Color.WHITE);
                contentStream.beginText();
                contentStream.newLineAtOffset(50, 40);
                contentStream.showText("Contactez-nous:");
                contentStream.newLineAtOffset(0, -15);
                contentStream.showText("Instagram: @edu_0trip");
                contentStream.newLineAtOffset(0, -15);
                contentStream.showText("Facebook: facebook.com/edu_0trip");
                contentStream.newLineAtOffset(0, -15);
                contentStream.showText("Téléphone: +216 55 179 732");
                contentStream.endText();
                contentStream.close();
            }

            // Sauvegarde du PDF
            String pdfDirectory = "C:/Users/maram/IdeaProjects/edutrip/src/main/resources/pdf/";
            new File(pdfDirectory).mkdirs();
            String pdfPath = pdfDirectory + "reservation_" + reservation.getId_reservationh() + ".pdf";
            document.save(pdfPath);
            System.out.println("✅ PDF généré avec QR Code à: " + pdfPath);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("❌ Erreur lors de la génération du PDF: " + e.getMessage());
        }
    }

    // Générateur de QR Code
    private void generateQRCode(String data, String filePath) {
        try {
            int width = 200;
            int height = 200;
            BitMatrix bitMatrix = new MultiFormatWriter().encode(data, BarcodeFormat.QR_CODE, width, height);
            Path path = Paths.get(filePath);
            MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("❌ Erreur lors de la génération du QR Code: " + e.getMessage());
        }
    }

    // Helper method to format dates (if needed)
    private String formatDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(date);
    }


    private void addStyledText(PDPageContentStream contentStream, String label, String value, float x, float y, Color color) throws IOException {
        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
        contentStream.setNonStrokingColor(color);
        contentStream.beginText();
        contentStream.newLineAtOffset(x, y);
        contentStream.showText(label + " ");
        contentStream.endText();

        contentStream.setFont(PDType1Font.HELVETICA, 12);
        contentStream.setNonStrokingColor(Color.BLACK);
        contentStream.beginText();
        contentStream.newLineAtOffset(x + 80, y);
        contentStream.showText(value);
        contentStream.endText();
    }

    private void handleDelete(Reservation_hebergement reservation) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de suppression");
        alert.setHeaderText("Supprimer Réservation");
        alert.setContentText("Êtes-vous sûr de vouloir supprimer cette réservation ?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            serviceReservation.remove(reservation.getId_reservationh());
            reservationList.remove(reservation);
            listViewReservation.refresh();
        }
    }

    private void handleUpdate(Reservation_hebergement reservation) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/UpdateReservation.fxml"));
            Parent root = loader.load();

            UpdateReservationController controller = loader.getController();
            controller.setReservation(reservation);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Modifier Réservation");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}