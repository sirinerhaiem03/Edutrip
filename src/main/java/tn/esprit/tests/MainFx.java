package tn.esprit.tests;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainFx extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/University/university.fxml"));//This line loads the new UI from the list
        Parent root = loader.load();//store new in

        // Get the actual width and height from the FXML
        double prefWidth = root.prefWidth(-1);
        double prefHeight = root.prefHeight(-1);


        Scene scene = new Scene(root, prefWidth, prefHeight);

        primaryStage.setTitle("University");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false); // Disable resizing if needed
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
