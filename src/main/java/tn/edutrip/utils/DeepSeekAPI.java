package tn.edutrip.utils;

import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

public class DeepSeekAPI {
    private static final String API_URL = "https://openrouter.ai/api/v1/chat/completions";
    private static final String API_KEY = "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";

    private OkHttpClient client;

    public DeepSeekAPI() {
        client = new OkHttpClient();
    }

    public String sendMessage(String message) throws IOException {
        MediaType JSON = MediaType.get("application/json; charset=utf-8");

        JSONObject jsonBody = new JSONObject();
        jsonBody.put("model", "deepseek/deepseek-r1:free");

        JSONArray messages = new JSONArray();
        JSONObject userMessage = new JSONObject();
        userMessage.put("role", "user");
        userMessage.put("content", message);
        messages.put(userMessage);

        jsonBody.put("messages", messages);

        RequestBody body = RequestBody.create(jsonBody.toString(), JSON);
        Request request = new Request.Builder()
                .url(API_URL)
                .post(body)
                .addHeader("Authorization", "Bearer " + API_KEY)
                .addHeader("HTTP-Referer", "<YOUR_SITE_URL>") // Optional
                .addHeader("X-Title", "<YOUR_SITE_NAME>") // Optional
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            JSONObject jsonResponse = new JSONObject(response.body().string());
            return jsonResponse.getJSONArray("choices").getJSONObject(0).getJSONObject("message").getString("content");
        }
    }
}