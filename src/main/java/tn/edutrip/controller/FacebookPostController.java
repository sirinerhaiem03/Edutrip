package tn.edutrip.controller;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class FacebookPostController {

    // Replace these with your actual values
    private static final String PAGE_ACCESS_TOKEN = "EAAYAZCCMkMpsBO6LUqklugwdu5meO05TZCmyXZBDuPQfihkjmZA5OWGegyhUefJHcZCQGeu7ROS0tVKB3UjDjcp7uOGhEt9K8SiZBKpbOAiepnf4t9MNdXQqE1WUy82p6zmGd4egCzhvvZA7ZCHc8uEdN1VDnXHi6MVDIM8SAXTlijuzKhW5dESCjlmjPU5oZBreDDrgL4mF6LNSYIZBKShAZDZD";
    private static final String FACEBOOK_GRAPH_URL = "https://graph.facebook.com/v12.0/me/feed";

    public void postToFacebook(String hebergementName, String hebergementType, String hebergementAddress, String hebergementPrice) {
        try {
            // Construct the post message
            String message = "Check out this amazing Hebergement: " + hebergementName + "\n" +
                    "Type: " + hebergementType + "\n" +
                    "Address: " + hebergementAddress + "\n" +
                    "Price: " + hebergementPrice + " TND";

            // Encode the message for the URL
            String postData = "message=" + URLEncoder.encode(message, StandardCharsets.UTF_8) +
                    "&access_token=" + PAGE_ACCESS_TOKEN;

            // Create the connection
            URL url = new URL(FACEBOOK_GRAPH_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            // Write the post data
            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = postData.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            // Get the response
            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                System.out.println("Post published successfully!");
            } else {
                System.err.println("Error publishing post: " + conn.getResponseMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error posting to Facebook: " + e.getMessage());
        }
    }
}
