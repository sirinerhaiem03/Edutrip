package tn.edutrip.controller;

import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import tn.edutrip.entities.Agence;

import java.text.SimpleDateFormat;
import java.util.*;

public class StatAgenceController {

    @FXML
    private BarChart<String, Number> barChart;
    @FXML
    private CategoryAxis xAxis;
    @FXML
    private NumberAxis yAxis;

    // Méthode pour générer les statistiques par année
    public void generateStatistiques(List<Agence> agences) {
        // Format pour extraire uniquement l'année
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");

        // Map pour stocker les statistiques par année
        Map<String, Integer> annualStats = new HashMap<>();

        // Grouper les agences par année
        for (Agence agence : agences) {
            // Extraire l'année de la date de création de l'agence
            String year = sdf.format(agence.getDateCreation());
            annualStats.put(year, annualStats.getOrDefault(year, 0) + 1);
        }

        // Créer la série de données pour le graphique
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Nombre d'agences par année");

        // Ajouter les données dans la série
        for (Map.Entry<String, Integer> entry : annualStats.entrySet()) {
            series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }

        // Ajouter la série au graphique
        barChart.getData().clear(); // Effacer les anciennes données
        barChart.getData().add(series);
    }
}
