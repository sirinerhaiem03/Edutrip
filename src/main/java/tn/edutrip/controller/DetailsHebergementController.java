package tn.edutrip.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import tn.edutrip.entities.Hebergement;

import java.io.File;

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

    public void setHebergement(Hebergement hebergement) {
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
    }
}
