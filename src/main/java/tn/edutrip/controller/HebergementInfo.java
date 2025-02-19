package tn.edutrip.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import tn.edutrip.entities.Hebergement;

public class HebergementInfo {

    @FXML
    private TextField nom;

    @FXML
    private TextField type;

    @FXML
    private TextField adresse;

    @FXML
    private TextField capacite;

    @FXML
    private TextField prix;

    @FXML
    private TextField disponible;

    @FXML
    private TextArea description;

    @FXML
    private ImageView image;

    public void setHebergement(Hebergement hebergement) {
        nom.setText(hebergement.getNomh());
        type.setText(hebergement.getTypeh());
        adresse.setText(hebergement.getAdressh());
        capacite.setText(String.valueOf(hebergement.getCapaciteh()));
        prix.setText(String.valueOf(hebergement.getPrixh()) + " $");
        disponible.setText(hebergement.getDisponibleh());
        description.setText(hebergement.getDescriptionh());

        // Load the image
        try {
            Image img = new Image(hebergement.getImageh());
            image.setImage(img);
        } catch (Exception e) {
            System.out.println("Invalid image URL: " + hebergement.getImageh());
        }
    }
}
