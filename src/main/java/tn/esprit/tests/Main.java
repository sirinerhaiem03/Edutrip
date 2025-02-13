package tn.esprit.tests;

import tn.esprit.entities.University;
import tn.esprit.entities.Candidature;
import tn.esprit.services.ServiceUniversity;
import tn.esprit.entities.EtatCandidature;
import tn.esprit.services.ServiceCandidature;

import java.sql.SQLException;
import java.util.List;

public class Main {
    public static void main(String[] args) throws SQLException {
        // Services for University and Candidature
        ServiceCandidature serviceCandidature = new ServiceCandidature();  // Assure-toi que cet objet est bien créé
        serviceCandidature = new ServiceCandidature();
        List<Candidature> candidatures = serviceCandidature.afficher();

        ServiceUniversity serviceUniversity = new ServiceUniversity();
        List<University> universities = serviceUniversity.afficher();
        serviceUniversity = new ServiceUniversity();



        // Create instances of University and Candidature
        University university1 = new University("Université de Tunis", "Tunis", "contact@utunis.tn", "Université publique d'enseignement supérieur.");
        University university2 = new University("Université de Monastir", "Monastir", "contact@umonastir.tn", "Université en sciences et technologies.");
        University nouvelleUniversite = new University("Université de Sfax", "Sfax", "contact@usfax.tn", "Université spécialisée en ingénierie.");

        Candidature candidature1 = new Candidature("cv1.pdf", "Motivation lettre 1", "Baccalauréat", EtatCandidature.EN_ATTENTE);
        Candidature candidature2 = new Candidature("cv2.pdf", "Motivation lettre 2", "Licence", EtatCandidature.ACCEPTEE);
        Candidature nouvelleCandidature = new Candidature("cv_new.pdf", "Lettre de motivation", "Licence", EtatCandidature.EN_ATTENTE);
        try {
            // Add universities and candidatures
            //serviceUniversity.ajouter(university1);
            //serviceUniversity.ajouter(university2);
            //serviceUniversity.ajouter(nouvelleUniversite);

            // Add candidatures
            //serviceCandidature.ajouter(candidature1);
            // serviceCandidature.ajouter(candidature2);
            // serviceCandidature.ajouter(nouvelleCandidature);

            // Modify a university and a candidature
            //serviceUniversity.modifier(university2);


            //University universityToUpdate = universities.get(universities.size() - 1); // Dernière ajoutée
           // universityToUpdate.setNom("Université de Sfax - Ingénierie");
           // universityToUpdate.setVille("Sfax");
            //universityToUpdate.setEmail("new-contact@usfax.tn");
           // universityToUpdate.setDescription("Université renommée en sciences et ingénierie.");
            // Appliquer la modification
            //serviceUniversity.modifier(universityToUpdate);
            //System.out.println("Liste des universités après modification : ");
            //System.out.println(serviceUniversity.afficher());

            // Modifier la candidature
            // Candidature candidatureToUpdate = candidatures.get(candidatures.size() - 1); // Dernière ajoutée
            //candidatureToUpdate.setCv("cv_updated.pdf");
            //candidatureToUpdate.setLettre_motivation("Nouvelle lettre de motivation");
            //candidatureToUpdate.setDiplome("Master");
            //candidatureToUpdate.setEtat(EtatCandidature.ACCEPTEE);
            // Appliquer la modification
            //serviceCandidature.modifier(candidatureToUpdate);
            // Afficher la liste mise à jour
            // System.out.println("Candidatures après modification : ");
            //System.out.println(serviceCandidature.afficher());


            //suppression
           // serviceCandidature.supprimer(16);
            // System.out.println("Candidatures après suppression : ");
            //System.out.println(serviceCandidature.afficher());


          //supression
            //serviceUniversity.supprimer(19);







            // Display the results
            System.out.println("Universities: ");
            System.out.println(serviceUniversity.afficher());

            System.out.println("Candidatures: ");
            System.out.println(serviceCandidature.afficher());

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
