package tn.edutrip.utils;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.BufferedReader;

public class CommentAnalysis {

    private static final String API_URL = "https://api.tgether.ai/comments/analyze"; // Remplace avec l'URL exacte
    private static final String API_KEY = "e96891fb9ec73316bc5a6257abb5215f3dc3899bcfc4f9c57ad88fc11bc107db"; // Remplace par ta clé API

    public static String analyzeComment(String commentText) {
        try {
            // Crée l'URL de la requête
            URL url = new URL(API_URL);

            // Ouvre la connexion HTTP
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Authorization", "Bearer " + API_KEY); // Si un Bearer token est nécessaire
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/json");

            // Prépare les données du corps de la requête en JSON
            String jsonInputString = "{\"comment\": \"" + commentText + "\"}";

            // Envoie les données via le flux de sortie
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            // Vérifie la réponse de l'API
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Lit la réponse de l'API
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                return response.toString(); // Renvoie la réponse de l'API
            } else {
                System.out.println("Erreur de la requête API : " + responseCode);
                return null;
            }
        } catch (Exception e) {
            System.out.println("Erreur lors de l'appel API : " + e.getMessage());
            return null;
        }
    }

    public static void main(String[] args) {
        // Exemple d'appel pour analyser un commentaire
        String comment = "C'est un excellent service, mais j'aimerais qu'il y ait plus de fonctionnalités.";
        String analysisResult = analyzeComment(comment);
        if (analysisResult != null) {
            System.out.println("Résultat de l'analyse : " + analysisResult);
        } else {
            System.out.println("Aucun résultat trouvé.");
        }
    }
}
