package tn.EduTrip.tests;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainFX extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/EtudiantVols.fxml"));
            Parent root = loader.load();
            
            Scene scene = new Scene(root, 800, 600);
            primaryStage.setTitle("EduTrip - Gestion des Vols");
            primaryStage.setScene(scene);
            primaryStage.show();
            
        } catch (Exception e) {
            System.err.println("Error loading FXML: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }


}
