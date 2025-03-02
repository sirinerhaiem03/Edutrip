package tn.edutrip.utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OpenAIModerationAPI {
    private static final String API_KEY = "sk-1234567890abcdef1234567890abcdef"; // Remplacez par votre clé API OpenAI valide
    private static final String API_URL = "https://api.openai.com/v1/moderations"; // URL de l'API OpenAI Moderation
    private static final int MAX_REQUESTS_PER_MINUTE = 20;
    private static final int MAX_RETRIES = 3;
    private static final long INITIAL_WAIT_TIME = 60000; // 60 seconds
    private static long lastRequestTime = 0;
    private static final Map<String, Boolean> cache = new HashMap<>();
    private static final Logger logger = Logger.getLogger(OpenAIModerationAPI.class.getName());

    public static boolean isContentAppropriate(String text) {
        // Vérifier le cache en premier
        if (cache.containsKey(text)) {
            logger.info("Résultat trouvé dans le cache.");
            return cache.get(text);
        }

        int retryCount = 0;
        while (retryCount < MAX_RETRIES) {
            try {
                waitIfNeeded(); // Respecter la limite de taux

                // Construire le corps de la requête
                JSONObject jsonInput = new JSONObject();
                jsonInput.put("input", text); // Texte à modérer

                // Envoyer la requête POST
                URL url = new URL(API_URL);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("Authorization", "Bearer " + API_KEY);
                connection.setDoOutput(true);

                // Écrire le corps de la requête
                try (OutputStream os = connection.getOutputStream()) {
                    byte[] input = jsonInput.toString().getBytes("utf-8");
                    os.write(input, 0, input.length);
                }

                // Lire la réponse
                int status = connection.getResponseCode();
                if (status == 429) {
                    long waitTime = INITIAL_WAIT_TIME * (1 << retryCount); // Backoff exponentiel
                    logger.warning("Erreur : Trop de requêtes. Attendre " + waitTime + " ms avant de réessayer.");
                    Thread.sleep(waitTime);
                    retryCount++;
                    continue;
                } else if (status != 200) {
                    logger.severe("Erreur HTTP : Code " + status);
                    // Lire la réponse d'erreur
                    BufferedReader errorReader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                    StringBuilder errorResponse = new StringBuilder();
                    String errorLine;
                    while ((errorLine = errorReader.readLine()) != null) {
                        errorResponse.append(errorLine);
                    }
                    errorReader.close();
                    logger.severe("Réponse d'erreur : " + errorResponse.toString());
                    return false;
                }

                // Lire la réponse JSON
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                logger.info("Réponse de l'API : " + response.toString());

                // Analyser la réponse JSON
                JSONObject jsonResponse = new JSONObject(response.toString());
                JSONArray results = jsonResponse.getJSONArray("results");
                if (results.length() > 0) {
                    JSONObject result = results.getJSONObject(0);
                    boolean isFlagged = result.getBoolean("flagged");

                    cache.put(text, !isFlagged); // Mettre en cache le résultat
                    return !isFlagged;
                }

                return true;

            } catch (Exception e) {
                logger.log(Level.SEVERE, "Erreur lors de la vérification du contenu : " + e.getMessage(), e);
                return false;
            }
        }
        return false;
    }

    private static void waitIfNeeded() throws InterruptedException {
        long currentTime = System.currentTimeMillis();
        long timeSinceLastRequest = currentTime - lastRequestTime;

        if (timeSinceLastRequest < TimeUnit.MINUTES.toMillis(1) / MAX_REQUESTS_PER_MINUTE) {
            long waitTime = TimeUnit.MINUTES.toMillis(1) / MAX_REQUESTS_PER_MINUTE - timeSinceLastRequest;
            logger.info("Attente de " + waitTime + " ms pour respecter la limite de taux.");
            Thread.sleep(waitTime);
        }

        lastRequestTime = System.currentTimeMillis();
    }

    public static void main(String[] args) {
        String testText = "Ceci est un commentaire inapproprié.";
        boolean isAppropriate = isContentAppropriate(testText);
        System.out.println("Le contenu est-il approprié ? " + isAppropriate);
    }
}