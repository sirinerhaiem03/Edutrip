package tn.EduTrip.services;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import tn.EduTrip.entites.Vol;

public class APIService {
    private static final String API_KEY = "TON_CLE_API";
    private static final String API_URL = "http://api.aviationstack.com/v1/flights?access_key=" + API_KEY;

    public List<Vol> recupererVolsDepuisAPI() {
        List<Vol> vols = new ArrayList<>();
        try {
            URL url = new URL(API_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                response.append(line);
            }
            br.close();

            JSONObject jsonResponse = new JSONObject(response.toString());
            JSONArray flights = jsonResponse.getJSONArray("data");

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZ");

            for (int i = 0; i < flights.length(); i++) {
                JSONObject flight = flights.getJSONObject(i);

                String numVol = flight.getJSONObject("flight").optString("iata", "N/A");
                String depart = flight.getJSONObject("departure").optString("airport", "Inconnu");
                String arrivee = flight.getJSONObject("arrival").optString("airport", "Inconnu");
                LocalDateTime dateDepart = LocalDateTime.parse(flight.getJSONObject("departure").optString("estimated", "2025-01-01T00:00:00+0000"), formatter);
                LocalDateTime dateArrivee = LocalDateTime.parse(flight.getJSONObject("arrival").optString("estimated", "2025-01-01T00:00:00+0000"), formatter);
                double prix = Math.random() * 500 + 50;  // Prix aléatoire entre 50€ et 500€
                int places = (int) (Math.random() * 100) + 50; // Places entre 50 et 150

                Vol vol = new Vol(numVol, depart, arrivee, dateDepart, dateArrivee, prix, places);
                vols.add(vol);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return vols;
    }
}
