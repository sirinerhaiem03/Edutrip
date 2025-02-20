package tn.edutrip.tests;

import tn.edutrip.entities.Commentaire;
import tn.edutrip.entities.Post;
import tn.edutrip.services.ServiceCommentaire;
import tn.edutrip.services.ServicePost;
import tn.edutrip.utils.MyDatabase;
import java.sql.Connection;
import java.sql.Date;
import java.util.List;

public class Main {
    public static void main(String[] args) {


        Connection conn = MyDatabase.getInstance().getConnection();


       ServicePost servicePost = new ServicePost();


       /* Post newPost = new Post(
                1, "Contenu de test", "2025-02-08", 123, "image.png", "universite"
        );

        Post newPost2 = new Post(
                1, "mon expeience", "2025-01-08", 13, "image.png", "hebergement"
        );
        //servicePost.add(newPost2);


        newPost.setContenu("bonjour a tous");
        newPost.setImage("image2.png");
        servicePost.update(newPost);*/
        servicePost.remove(11);

       /* List<Post> posts = servicePost.getAll();


        if (posts.isEmpty()) {
            System.out.println("Aucun post trouvé.");
        } else {
            for (Post post : posts) {

                System.out.println("ID du Post: " + post.getId_post());
                System.out.println("Contenu: " + post.getContenu());
                System.out.println("Date de création: " + post.getDate_creation());
                System.out.println("ID de l'étudiant: " + post.getId_etudiant());
                System.out.println("Image: " + post.getImage());
                System.out.println("Catégorie: " + post.getCategorie());

            }
        }


*/






     /*   ServiceCommentaire serviceCommentaire = new ServiceCommentaire();
        String dateString = "2025-02-07";

        // Convertit la chaîne en un objet java.sql.Date
        Date dateCommentaire = Date.valueOf(dateString);

        // Ajout d'un commentaire
        Commentaire newCommentaire = new Commentaire(
                7, // ID commentaire sera généré automatiquement
                6, // ID du post auquel ce commentaire est lié
                123, // ID de l'étudiant
                "experience....", // Contenu du commentaire
                dateCommentaire // Date du commentaire (date actuelle)
        );

        // Ajouter le commentaire dans la base de données
        //serviceCommentaire.add(newCommentaire);
// Mise à jour d'un commentaire existant
        newCommentaire.setContenu("C'est un super post, vraiment intéressant !");
        serviceCommentaire.update(newCommentaire);

        // Suppression d'un commentaire par ID (ici on utilise l'ID 1 comme exemple)
        serviceCommentaire.remove(3);

        // Récupération et affichage de tous les commentaires
        List<Commentaire> commentaires = serviceCommentaire.getAll();

        if (commentaires.isEmpty()) {
            System.out.println("Aucun commentaire trouvé.");
        } else {
            for (Commentaire commentaire : commentaires) {
                System.out.println("ID du Commentaire: " + commentaire.getId_commentaire());
                System.out.println("ID du Post: " + commentaire.getId_post());
                System.out.println("ID de l'étudiant: " + commentaire.getId_etudiant());
                System.out.println("Contenu: " + commentaire.getContenu());
                System.out.println("Date du commentaire: " + commentaire.getDate_commentaire());

            }
        }

*/

        servicePost.getPostsWithComments();
    }


}
