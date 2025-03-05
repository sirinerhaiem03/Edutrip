package tn.esprit.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.stage.Stage;
import tn.esprit.entities.Candidature;
import tn.esprit.services.ServiceCandidature;
import tn.esprit.utils.PaymentProcess;

import java.io.IOException;

public class PaymentController {

    @FXML public Button refresh_btn;
    @FXML public Button back_btn;
    @FXML private Label lblName;
    @FXML private Label lblEmail;
    @FXML private Label lblAmount;
    @FXML private Button pay_btn;
    @FXML private ProgressIndicator progressIndicator;
    @FXML private Label successLabel;


    
    private String userName;
    private String userEmail;
    private float amount;
    private Candidature candidature;
    private final ServiceCandidature serviceCandidature = new ServiceCandidature();
    public void setPaymentDetails(String userName, String userEmail, float amount, Candidature candidature) {
        this.userName = userName;
        this.userEmail = userEmail;
        this.amount = amount;
        this.candidature = candidature;

        // Update labels with payment details
        lblName.setText("Name: " + userName);
        lblEmail.setText("Email: " + userEmail);
        lblAmount.setText("Amount: $" + amount);
    }
    @FXML
    public void initialize() {
        // Hide the refresh button initially
        refresh_btn.setVisible(false);
    }

    @FXML
    private void handlePay() {
        try {
            // Redirect to Stripe Checkout
            String checkoutUrl = PaymentProcess.createCheckoutSession(amount);//to generate a Stripe payment URL.
            openUrl(checkoutUrl);

            // Show a message to the user
            showAlert("Payment", "Please complete the payment on the Stripe page and return to this application.");

            // Hide the Pay Now button and show the Refresh button
            pay_btn.setVisible(false);
            refresh_btn.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "An error occurred during payment.");
        }
    }

    private void openUrl(String url) {
        try {
            java.awt.Desktop.getDesktop().browse(java.net.URI.create(url));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveCandidature() {
        try {
            if (candidature != null) {
                serviceCandidature.ajouter(candidature);////
                redirectToListCandidatures();
            } else {
                showAlert("Error", "Error saving candidature.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Error saving candidature.");
        }
    }
    private void redirectToListCandidatures() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Candidature/Candidature.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) back_btn.getScene().getWindow();
        stage.setScene(new Scene(root));
    }

    @FXML
    private void redirectToAjouterCandidature() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Candidature/ajouter.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) back_btn.getScene().getWindow();
        stage.setScene(new Scene(root));
    }

    @FXML
    private void handleRefresh() {
        saveCandidature();
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}