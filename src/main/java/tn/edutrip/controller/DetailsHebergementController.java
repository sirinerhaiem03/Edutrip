package tn.edutrip.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import tn.edutrip.entities.Hebergement;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DetailsHebergementController {

    @FXML
    private ImageView imageHebergement;

    @FXML
    private Label labelNom;

    @FXML
    private Label labelType;

    @FXML
    private Label labelAdresse;

    @FXML
    private Label labelCapacite;

    @FXML
    private Label labelDisponible;

    @FXML
    private Label labelPrix;

    @FXML
    private Label labelDescription;

    @FXML
    private Button pdfid; // Add this field

    private Hebergement hebergement; // Store the Hebergement object

    public void setHebergement(Hebergement hebergement) {
        this.hebergement = hebergement; // Store the Hebergement object

        labelNom.setText(hebergement.getNomh());
        labelType.setText(hebergement.getTypeh());
        labelAdresse.setText(hebergement.getAdressh());
        labelCapacite.setText(hebergement.getCapaciteh() + " personnes");
        labelDisponible.setText(hebergement.getDisponibleh());
        labelPrix.setText(hebergement.getPrixh() + " TND");
        labelDescription.setText(hebergement.getDescriptionh());

        // Set Image Path
        String imagePath = "C:/Users/maram/IdeaProjects/EDUTRIP3/src/main/resources/images/" + hebergement.getImageh();
        File imageFile = new File(imagePath);

        if (imageFile.exists()) {
            imageHebergement.setImage(new Image(imageFile.toURI().toString()));
        } else {
            imageHebergement.setImage(new Image("file:/C:/Users/maram/IdeaProjects/EDUTRIP3/src/main/resources/images/default_hebergement.png"));
        }

        // Add an event handler to the PDF button
        pdfid.setOnAction(event -> generatePDF());
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
                String logoPath = "C:/Users/maram/IdeaProjects/EDUTRIP3/src/main/resources/images/logo.png";
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
                String imagePath = "C:/Users/maram/IdeaProjects/EDUTRIP3/src/main/resources/images/" + hebergement.getImageh();
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
            String pdfDirectory = "C:/Users/maram/IdeaProjects/EDUTRIP3/src/main/resources/pdf/";
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