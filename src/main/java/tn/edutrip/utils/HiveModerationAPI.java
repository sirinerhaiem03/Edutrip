package tn.edutrip.utils;

import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HiveModerationAPI {
  /*  private static final String API_KEY = "xmHln3cD54rWuN3lI2Iwwam6LkrRPwYR"; // Remplacez par votre clé API
    private static final String API_URL = "https://api.thehive.ai/api/v2/task/sync";
    private static final Logger logger = Logger.getLogger(HiveModerationAPI.class.getName());
    private static final Map<String, Boolean> cache = new HashMap<>();

    public static boolean isContentAppropriate(String text) {
        // Vérifier si le texte est vide
        if (text == null || text.trim().isEmpty()) {
            logger.warning("Le texte à modérer est vide.");
            return true; // ou false, selon votre logique
        }

        // Vérifier si le texte est déjà dans le cache
        if (cache.containsKey(text)) {
            logger.info("Résultat trouvé dans le cache.");
            return cache.get(text);
        }

        try {
            // Préparer la requête JSON
            JSONObject jsonInput = new JSONObject();
            jsonInput.put("text", text);
            jsonInput.put("options", new JSONObject().put("tasks", new String[]{"text_moderation"}));

            logger.info("Requête JSON : " + jsonInput.toString());

            // Créer la connexion HTTP
            URL url = new URL(API_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Authorization", "Bearer " + API_KEY);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            // Envoyer la requête
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonInput.toString().getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            // Vérifier le code de statut de la réponse
            int status = connection.getResponseCode();
            if (status != 200) {
                logger.severe("Erreur HTTP: Code " + status);
                // Lire le message d'erreur de la réponse
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

            // Lire la réponse de l'API
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
            JSONObject textModeration = jsonResponse.getJSONObject("results").getJSONObject("text_moderation");

            // Vérifier si le contenu est inapproprié
            boolean isAppropriate = true;
            for (String key : textModeration.keySet()) {
                double score = textModeration.getJSONObject(key).getDouble("score");
                logger.info("Catégorie : " + key + ", Score : " + score);
                if (score > 0.5) { // Seuil de tolérance
                    isAppropriate = false;
                    break;
                }
            }

            // Mettre en cache le résultat
            cache.put(text, isAppropriate);
            return isAppropriate;

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Erreur lors de la vérification du contenu : " + e.getMessage(), e);
            return false;
        }
    }*/
}