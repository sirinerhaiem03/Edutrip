package tn.EduTrip.utils;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;



import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

    public class AviationStackService {

        private static final String API_KEY = "e9e4c966e460a01641acc733b7878d48";
        private static final String API_URL = "https://api.aviationstack.com/v1/flights";

        public static JSONArray getFlights() {
            try {
                String fullUrl = API_URL + "?access_key=" + API_KEY + "&limit=10";
                URL url = new URL(fullUrl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");

                int responseCode = conn.getResponseCode();
                if (responseCode != 200) {
                    throw new RuntimeException("Erreur HTTP : " + responseCode);
                }

                Scanner scanner = new Scanner(conn.getInputStream());
                StringBuilder jsonResponse = new StringBuilder();
                while (scanner.hasNext()) {
                    jsonResponse.append(scanner.nextLine());
                }
                scanner.close();

                JSONObject response = new JSONObject(jsonResponse.toString());

                // Vérifier la présence d'erreurs dans la réponse
                if (response.has("error")) {
                    JSONObject error = response.getJSONObject("error");
                    System.err.println("Erreur API: " + error.getString("message"));
                    return new JSONArray();
                }

                // Vérifier si la clé 'data' existe
                if (!response.has("data")) {
                    System.err.println("Aucune donnée disponible dans la réponse");
                    return new JSONArray();
                }

                return response.getJSONArray("data");

            } catch (IOException e) {
                System.err.println("Erreur de connexion: " + e.getMessage());
                return new JSONArray();
            } catch (JSONException e) {
                System.err.println("Erreur de parsing JSON: " + e.getMessage());
                return new JSONArray();
            }
        }
    }


