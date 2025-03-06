package tn.edutrip.utils;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
public class BadWordsAPI {

    private static final String API_KEY = "TGAqcNYuFFY4CUq5wEq40Q==2vpPucA3zSBJ46en"; // Remplacez par votre clé API Ninja
    private static final String API_URL = "https://api.api-ninjas.com/v1/profanityfilter?text=";

    public static boolean containsBadWords(String text) {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            String encodedText = java.net.URLEncoder.encode(text, "UTF-8");
            String requestUrl = API_URL + encodedText;

            HttpGet request = new HttpGet(requestUrl);
            request.setHeader("X-Api-Key", API_KEY);

            HttpResponse response = httpClient.execute(request);
            String responseBody = EntityUtils.toString(response.getEntity());

            // Analyser la réponse JSON
            JSONObject jsonResponse = new JSONObject(responseBody);
            return jsonResponse.getBoolean("has_profanity"); // Retourne true si le texte contient des mots inappropriés
        } catch (Exception e) {
            e.printStackTrace();
            return false; // En cas d'erreur, retourne false
        }
    }

    public static void main(String[] args) {
        String testText = "This is a bad word test.";
        boolean hasBadWords = containsBadWords(testText);
        System.out.println("Le texte contient-il des mots inappropriés ? " + hasBadWords);
    }
}
