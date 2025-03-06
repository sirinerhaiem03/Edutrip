package tn.edutrip.utils;

import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;

public class MyMemoryTranslateAPI {
    private static final String API_URL = "https://api.mymemory.translated.net/get";

    public static String translateText(String text, String sourceLanguage, String targetLanguage) {
        try {
            // Encoder le texte pour l'URL
            String encodedText = URLEncoder.encode(text, "UTF-8");

            // Construire l'URL de la requête
            String requestUrl = API_URL + "?q=" + encodedText + "&langpair=" + sourceLanguage + "|" + targetLanguage;

            // Envoyer la requête GET
            URL url = new URL(requestUrl);
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            // Analyser la réponse JSON
            JSONObject jsonResponse = new JSONObject(response.toString());
            return jsonResponse.getJSONObject("responseData").getString("translatedText");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        String text = "Hello, how are you?";
        String translatedText = translateText(text, "en", "fr"); // Traduire de l'anglais vers le français
        System.out.println("Texte traduit : " + translatedText);
    }
}