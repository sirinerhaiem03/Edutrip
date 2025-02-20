package tn.edutrip.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import tn.edutrip.entities.Commentaire;
import tn.edutrip.services.ServiceCommentaire;

import java.util.Optional;

public class CommentsItemsController {

    @FXML
    private Text CommentAuthor;

    @FXML
    private Text CommentDate;

    @FXML
    private Text CommentContent;

    @FXML
    private ImageView menuIcon; // Icône des trois points

    private Commentaire currentComment; // Commentaire actuel
    private ServiceCommentaire serviceCommentaire; // Service pour gérer les commentaires

    public CommentsItemsController() {
        serviceCommentaire = new ServiceCommentaire(); // Initialiser le service
    }

    public void setCommentData(Commentaire commentaire) {
        currentComment = commentaire;

        // Afficher les données du commentaire
        CommentAuthor.setText("Auteur ID: " + commentaire.getId_etudiant());
        CommentDate.setText(commentaire.getDate_commentaire().toString());
        CommentContent.setText(commentaire.getContenu());

        // Configurer le menu contextuel
        setupContextMenu();
    }

    private void setupContextMenu() {
        // Créer un menu contextuel
        ContextMenu contextMenu = new ContextMenu();

        // Option "Modifier"
        MenuItem editItem = new MenuItem("Modifier");
        editItem.setOnAction(event -> modifierCommentaire());

        // Option "Supprimer"
        MenuItem deleteItem = new MenuItem("Supprimer");
        deleteItem.setOnAction(event -> supprimerCommentaire());

        // Ajouter les options au menu
        contextMenu.getItems().addAll(editItem, deleteItem);

        // Associer le menu contextuel à l'icône des trois points
        menuIcon.setOnMouseClicked(event -> contextMenu.show(menuIcon, event.getScreenX(), event.getScreenY()));
    }

    private void modifierCommentaire() {
        // Ouvrir une boîte de dialogue pour modifier le commentaire
        TextInputDialog dialog = new TextInputDialog(currentComment.getContenu());
        dialog.setTitle("Modifier le commentaire");
        dialog.setHeaderText("Modifiez le contenu du commentaire :");
        dialog.setContentText("Contenu :");

        // Afficher la boîte de dialogue et attendre la réponse de l'utilisateur
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(newContent -> {
            // Mettre à jour le commentaire dans la base de données
            currentComment.setContenu(newContent);
            serviceCommentaire.update(currentComment);
            System.out.println("Commentaire modifié : " + currentComment.getId_commentaire());

            // Rafraîchir l'affichage du commentaire
            CommentContent.setText(newContent);
        });
    }

    private void supprimerCommentaire() {
        // Supprimer le commentaire de la base de données
        serviceCommentaire.remove(currentComment.getId_commentaire());
        System.out.println("Commentaire supprimé : " + currentComment.getId_commentaire());

        // Rafraîchir l'affichage des commentaires (si nécessaire)
        // Par exemple, vous pouvez notifier le contrôleur parent pour qu'il mette à jour la liste des commentaires
    }
}