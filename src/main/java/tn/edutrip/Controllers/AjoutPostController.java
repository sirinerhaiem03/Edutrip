package tn.edutrip.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import tn.edutrip.entities.Post;
import tn.edutrip.services.ServicePost;

import java.io.File;

public class AjoutPostController {

    @FXML
    private TextField IdCategorie;

    @FXML
    private TextField IdContenu;

    @FXML
    private ImageView idImage;

    private String imagePath; // Pour stocker le chemin de l'image sélectionnée

    private ServicePost servicePost; // Référence au service

    public AjoutPostController() {
        servicePost = new ServicePost(); // Initialiser le service
    }

    @FXML
    void AjouterPost(ActionEvent event) {
        // Récupérer les données saisies par l'utilisateur
        String categorie = IdCategorie.getText();
        String contenu = IdContenu.getText();

        // Créer un nouvel objet Post
        Post post = new Post();
        post.setCategorie(categorie);
        post.setContenu(contenu);
        post.setImage(imagePath); // Utiliser le chemin de l'image sélectionnée
        post.setId_etudiant(1); // Remplacez par l'ID de l'étudiant connecté
        post.setDate_creation(java.time.LocalDate.now().toString()); // Date actuelle

        // Ajouter le post via le service
        servicePost.add(post);


        System.out.println("Post ajoute avec succes !");
    }

    @FXML
    void choisirImage(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir une image");
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            imagePath = file.getAbsolutePath();


            Image image = new Image(file.toURI().toString());
            idImage.setImage(image);
        }
    }
}