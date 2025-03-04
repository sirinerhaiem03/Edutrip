package tn.edutrip.services;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class ChatbotService {
    private static final String API_KEY = System.getenv("HUGGINGFACE_TOKEN");
    private static final String MODEL_URL = "https://api-inference.huggingface.co/models/facebook/blenderbot-3B";

    public static String getChatbotResponse(String userMessage) {
        try {
            URL url = new URL(MODEL_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "Bearer " + API_KEY);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            // 🔹 JSON request
            String jsonInput = "{ \"inputs\": \"" + userMessage + "\" }";
            try (OutputStream os = conn.getOutputStream()) {
                os.write(jsonInput.getBytes());
                os.flush();
            }

            // 🔹 Read the response
            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                try (Scanner scanner = new Scanner(conn.getInputStream())) {
                    return scanner.useDelimiter("\\A").next();
                }
            } else {
                return "⚠️ Error: API response code " + responseCode;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "❌ Error: Unable to connect to AI chatbot.";
        }
    }

    public static void main(String[] args) {
        System.out.println(getChatbotResponse("Hello, how are you?"));
    }
}
