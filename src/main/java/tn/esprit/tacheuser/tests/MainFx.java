package tn.esprit.tacheuser.tests;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainFx extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Login.fxml"));
        try {
            Parent parent = loader.load();
            Scene scene = new Scene(parent);
            primaryStage.setScene(scene);
          //  primaryStage.setMaximized(true); // Ouvre la fenêtre en plein écran
           // primaryStage.setMaximized(true); // Plein écran
          //  primaryStage.setMaximized(true); // Mettre l'application en plein écran
            //primaryStage.setFullScreen(true);
           // primaryStage.setFullScreen(true);// 2eme chance force


            primaryStage.setTitle("LoginController");
            primaryStage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }
}
