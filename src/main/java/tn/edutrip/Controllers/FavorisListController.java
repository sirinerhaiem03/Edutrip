package tn.edutrip.Controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import tn.edutrip.entities.Post;
import tn.edutrip.services.ServiceFavoris;
import tn.edutrip.services.ServicePost;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class FavorisListController implements Initializable {

    @FXML
    private ListView<Post> favorisListView; // ListView pour afficher les posts favoris

    private ServiceFavoris serviceFavoris = new ServiceFavoris();
    private ServicePost servicePost = new ServicePost();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // ID de l'étudiant (ici, on utilise 1 pour l'exemple)
        int idEtudiant = 1;

        // Charger les posts favoris
        loadFavoris(idEtudiant);
    }
    public void loadFavoris(int idEtudiant) {
    // Utiliser directement l'ID de l'étudiant

        // Récupérer la liste des IDs des posts favoris
        List<Integer> favorisIds = serviceFavoris.getFavorisByEtudiant(idEtudiant);
        System.out.println("IDs des favoris récupérés : " + favorisIds);

        // Vider la ListView avant de la remplir
        favorisListView.getItems().clear();

        // Récupérer et afficher les posts favoris
        for (int idPost : favorisIds) {
            Post post = servicePost.getPostById(idPost);
            if (post != null) {
                System.out.println("Post trouvé : " + post.getId_post() + " - " + post.getContenu());
                favorisListView.getItems().add(post); // Ajouter le post à la ListView
            } else {
                System.err.println("Post non trouvé pour l'ID : " + idPost);
            }
        }

        // Définir un CellFactory pour personnaliser l'affichage des posts
        favorisListView.setCellFactory(param -> new ListCell<Post>() {
            @Override
            protected void updateItem(Post post, boolean empty) {
                super.updateItem(post, empty);

                if (empty || post == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    try {
                        // Charger le fichier FXML de PostItem
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/PostItem.fxml"));
                        VBox vbox = loader.load();

                        // Obtenir le contrôleur de PostItem
                        PostItemController controller = loader.getController();
                        controller.setPostData(post, favorisListView); // Passer les données du post

                        // Afficher le post dans la cellule
                        setGraphic(vbox);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }}