package tn.edutrip.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.json.JSONArray;
import org.json.JSONObject;
import tn.edutrip.entities.Hebergement;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List; // Ensure you import java.util.List


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class DetailsHebergementController {

    @FXML
    private ImageView imageHebergement;
    @FXML
    private Label labelNom, labelType, labelAdresse, labelCapacite, labelDisponible, labelPrix, labelDescription;
    @FXML
    private Button pdfid, mapid, facebookShareButton, twitterShareButton, instagramShareButton;
    @FXML
    private VBox eventsContainer;

    private Hebergement hebergement;
    private static final String API_KEY = "zOKd8WYOzQIYaThCoNRwSwKG31pBTsGj";  // Replace with your actual API key

    public void setHebergement(Hebergement hebergement) {
        this.hebergement = hebergement;

        if (hebergement == null) {
            System.err.println("Hebergement object is null.");
            return;
        }

        // Set UI labels
        labelNom.setText(hebergement.getNomh());
        labelType.setText(hebergement.getTypeh());
        labelAdresse.setText(hebergement.getAdressh());
        labelCapacite.setText(hebergement.getCapaciteh() + " personnes");
        labelDisponible.setText(hebergement.getDisponibleh());
        labelPrix.setText(hebergement.getPrixh() + " TND");
        labelDescription.setText(hebergement.getDescriptionh());

        // Load image
        loadImage();

        // Fetch local events
        fetchLocalEvents();

        pdfid.setOnAction(event -> generatePDF());
        mapid.setOnAction(event -> openGoogleMaps());
        facebookShareButton.setOnAction(event -> shareOnFacebook());
        twitterShareButton.setOnAction(event -> shareOnTwitter());
        instagramShareButton.setOnAction(event -> shareOnInstagram());

    }

    private void fetchLocalEvents() {
        if (hebergement == null || hebergement.getAdressh() == null || hebergement.getAdressh().isEmpty()) {
            System.err.println("No address available for this hebergement.");
            return;
        }

        new Thread(() -> {
            try {
                List<String> events = fetchEventsFromTicketmaster(hebergement.getAdressh());
                Platform.runLater(() -> {
                    eventsContainer.getChildren().clear();
                    if (events.isEmpty()) {
                        eventsContainer.getChildren().add(new Label("No events found near this location."));
                    } else {
                        for (String event : events) {
                            Button eventButton = new Button(event);
                            eventButton.setOnAction(e -> System.out.println("Clicked on: " + event));
                            eventsContainer.getChildren().add(eventButton);
                        }
                    }
                });
            } catch (Exception e) {
                System.err.println("Error fetching events: " + e.getMessage());
            }
        }).start();
    }

    private List<String> fetchEventsFromTicketmaster(String location) {
        List<String> events = new ArrayList<>();
        try {
            String encodedLocation = URLEncoder.encode(location, StandardCharsets.UTF_8);
            String urlString = "https://app.ticketmaster.com/discovery/v2/events.json?apikey=" + API_KEY + "&city=" + encodedLocation;
            HttpURLConnection conn = (HttpURLConnection) new URL(urlString).openConnection();
            conn.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) response.append(line);
            reader.close();

            JSONObject jsonResponse = new JSONObject(response.toString());
            JSONObject embedded = jsonResponse.optJSONObject("_embedded");

            if (embedded != null) {
                JSONArray eventsArray = embedded.getJSONArray("events");
                for (int i = 0; i < eventsArray.length(); i++) {
                    events.add(eventsArray.getJSONObject(i).getString("name"));
                }
            }
        } catch (Exception e) {
            System.err.println("Error fetching events from Ticketmaster: " + e.getMessage());
        }
        return events;
    }


    private void loadImage() {
        String imagePath = "C:/Users/maram/IdeaProjects/edutrip/src/main/resources/images/" + hebergement.getImageh();
        File imageFile = new File(imagePath);
        String defaultImagePath = "file:/C:/Users/maram/IdeaProjects/edutrip/src/main/resources/images/default_hebergement.png";

        imageHebergement.setImage(new Image(imageFile.exists() ? imageFile.toURI().toString() : defaultImagePath));
    }





    private void openURL(String url) {
        try {
            Desktop.getDesktop().browse(URI.create(url));
        } catch (IOException e) {
            e.printStackTrace();
        }}
    private void openGoogleMaps() {
        String address = hebergement.getAdressh();
        String encodedAddress = URLEncoder.encode(address, StandardCharsets.UTF_8);
        String googleMapsUrl = "https://www.google.com/maps/search/?api=1&query=" + encodedAddress;

        // Create a new WebView
        WebView webView = new WebView();
        WebEngine webEngine = webView.getEngine();
        webEngine.load(googleMapsUrl);

        // Create a new Stage (pop-up window)
        Stage mapStage = new Stage();
        mapStage.setTitle("Google Maps - " + hebergement.getNomh());
        mapStage.setScene(new Scene(webView, 800, 600));
        mapStage.show();
    }

    private void shareOnFacebook() {
        try {
            String url = "https://www.facebook.com/sharer/sharer.php?u=" + URLEncoder.encode("https://www.edutrip.com/hebergement/" + hebergement.getId_hebergement(), StandardCharsets.UTF_8);
            Desktop.getDesktop().browse(URI.create(url));
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error sharing on Facebook: " + e.getMessage());
        }
    }

    private void shareOnTwitter() {
        try {
            String text = URLEncoder.encode("Check out this amazing Hebergement: " + hebergement.getNomh(), StandardCharsets.UTF_8);
            String url = "https://twitter.com/intent/tweet?text=" + text + "&url=" + URLEncoder.encode("https://www.edutrip.com/hebergement/" + hebergement.getId_hebergement(), StandardCharsets.UTF_8);
            Desktop.getDesktop().browse(URI.create(url));
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error sharing on Twitter: " + e.getMessage());
        }

    }

    private void shareOnInstagram() {
        try {
            Desktop.getDesktop().browse(URI.create("https://www.instagram.com/"));
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error opening Instagram: " + e.getMessage());
        }
    }
    private void generatePDF() {
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                float pageWidth = page.getMediaBox().getWidth();
                float pageHeight = page.getMediaBox().getHeight();

                // Couleurs
                java.awt.Color themeBlue = new java.awt.Color(87, 160, 210); // #57A0D2
                java.awt.Color backgroundColor = new java.awt.Color(226, 240, 254); // #E2F0FE

                // Arrière-plan du PDF
                contentStream.setNonStrokingColor(backgroundColor);
                contentStream.addRect(0, 0, pageWidth, pageHeight);
                contentStream.fill();

                // En-tête (bleu couvrant toute la largeur)
                float headerHeight = 70;
                contentStream.setNonStrokingColor(themeBlue);
                contentStream.addRect(0, pageHeight - headerHeight, pageWidth, headerHeight);
                contentStream.fill();

                // Logo plus grand à gauche
                String logoPath = "C:/Users/maram/IdeaProjects/edutrip/src/main/resources/images/logo.png";
                File logoFile = new File(logoPath);
                if (logoFile.exists()) {
                    PDImageXObject logo = PDImageXObject.createFromFile(logoPath, document);
                    contentStream.drawImage(logo, 20, pageHeight - 60, 120, 50);
                }

                // Titre du PDF (centré et plus visible)
                contentStream.setNonStrokingColor(java.awt.Color.WHITE);
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 18);
                contentStream.beginText();
                contentStream.newLineAtOffset((pageWidth / 2) - 80, pageHeight - 45);
                contentStream.showText("Détails de l'Hébergement");
                contentStream.endText();

                float yPosition = pageHeight - 140;

                // Image de l'hébergement (centrée)
                String imagePath = "C:/Users/maram/IdeaProjects/edutrip/src/main/resources/images/" + hebergement.getImageh();
                File imageFile = new File(imagePath);
                if (imageFile.exists()) {
                    BufferedImage bufferedImage = ImageIO.read(imageFile);
                    PDImageXObject image = LosslessFactory.createFromImage(document, bufferedImage);
                    float imageWidth = 250;
                    float imageHeight = 180;
                    contentStream.drawImage(image, (pageWidth - imageWidth) / 2, yPosition - imageHeight, imageWidth, imageHeight);
                    yPosition -= (imageHeight + 20);
                }

                // Ligne de séparation
                contentStream.setStrokingColor(themeBlue);
                contentStream.moveTo(50, yPosition);
                contentStream.lineTo(pageWidth - 50, yPosition);
                contentStream.stroke();
                yPosition -= 20;

                // Centrage des détails
                float textXPosition = pageWidth / 2 - 100;

                addStyledText(contentStream, "Nom:", hebergement.getNomh(), textXPosition, yPosition, themeBlue);
                yPosition -= 20;
                addStyledText(contentStream, "Type:", hebergement.getTypeh(), textXPosition, yPosition, themeBlue);
                yPosition -= 20;
                addStyledText(contentStream, "Adresse:", hebergement.getAdressh(), textXPosition, yPosition, themeBlue);
                yPosition -= 20;
                addStyledText(contentStream, "Capacité:", hebergement.getCapaciteh() + " personnes", textXPosition, yPosition, themeBlue);
                yPosition -= 20;
                addStyledText(contentStream, "Disponible:", hebergement.getDisponibleh(), textXPosition, yPosition, themeBlue);
                yPosition -= 20;
                addStyledText(contentStream, "Prix:", hebergement.getPrixh() + " TND", textXPosition, yPosition, themeBlue);
                yPosition -= 20;
                addStyledText(contentStream, "Description:", hebergement.getDescriptionh(), textXPosition, yPosition, themeBlue);
                yPosition -= 30;

                // Pied de page (bleu couvrant toute la largeur)
                float footerHeight = 70;
                contentStream.setNonStrokingColor(themeBlue);
                contentStream.addRect(0, 0, pageWidth, footerHeight);
                contentStream.fill();

                // Texte du pied de page (Contact)
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

                // Fermeture du flux
                contentStream.close();
            }

            // Sauvegarde du PDF
            String pdfDirectory = "C:/Users/maram/IdeaProjects/edutrip/src/main/resources/pdf/";
            File directory = new File(pdfDirectory);
            if (!directory.exists()) {
                directory.mkdirs();
            }
            String pdfPath = pdfDirectory + "hebergement_" + hebergement.getId_hebergement() + ".pdf";
            document.save(pdfPath);
            System.out.println("PDF généré avec succès à: " + pdfPath);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Erreur lors de la génération du PDF: " + e.getMessage());
        }
    }

    // Méthode pour afficher les détails centrés sous l'image
    private void addStyledText(PDPageContentStream contentStream, String label, String value, float x, float y, java.awt.Color color) throws IOException {
        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
        contentStream.setNonStrokingColor(color);
        contentStream.beginText();
        contentStream.newLineAtOffset(x, y);
        contentStream.showText(label);
        contentStream.endText();

        contentStream.setFont(PDType1Font.HELVETICA, 12);
        contentStream.setNonStrokingColor(java.awt.Color.BLACK);
        contentStream.beginText();
        contentStream.newLineAtOffset(x + 100, y);
        contentStream.showText(value);
        contentStream.endText();
    }



}