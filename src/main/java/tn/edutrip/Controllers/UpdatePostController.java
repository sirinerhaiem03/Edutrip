package tn.edutrip.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import tn.edutrip.entities.Post;
import tn.edutrip.services.ServicePost;

public class UpdatePostController {

    @FXML
    private TextField txtCategorie;

    @FXML
    private TextField txtContenu;

    private final ServicePost servicePost = new ServicePost();
    private Post currentPost;
    private AffichagePostController affichagePostController;

    public void setPost(Post post, AffichagePostController affichagePostController) {
        this.currentPost = post;
        this.affichagePostController = affichagePostController;

        // Mettre à jour les champs de texte avec les informations du post
        txtCategorie.setText(post.getCategorie());
        txtContenu.setText(post.getContenu());
    }

    @FXML
    public void validerModification() {

        // Récupérer les données modifiées
        String updatedContenu = txtContenu.getText();
        String updatedCategorie = txtCategorie.getText();

        // Mettre à jour le post
        currentPost.setContenu(updatedContenu);
        currentPost.setCategorie(updatedCategorie);
        servicePost.update(currentPost);  // Mettre à jour dans la base de données



        // Fermer la fenêtre de modification
        Stage stage = (Stage) txtContenu.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void annulerModification() {
        Stage stage = (Stage) txtContenu.getScene().getWindow();
        stage.close();
    }
    public void setAffichagePostController(AffichagePostController affichagePostController) {
        this.affichagePostController = affichagePostController;
    }
}
