package tn.esprit.services;

import tn.esprit.entities.University;
import tn.esprit.utils.MyDatabase;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

public class ServiceUniversity {

    private Connection connection = MyDatabase.getInstance().getConnection();//MyDatabase singleton instance. all db use same conn
    private static final String API_URL = "http://universities.hipolabs.com/search";//uni info



    public void ajouter(University university) throws SQLException {
        String query = "INSERT INTO university (nom, ville, email, description) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) { //prevent sql injection
            stmt.setString(1, university.getNom());//Sets the values dynamically
            stmt.setString(2, university.getVille());
            stmt.setString(3, university.getEmail());
            stmt.setString(4, university.getDescription());
            stmt.executeUpdate();
        }
    }


    public void update(University university) throws SQLException {
        String query = "UPDATE university SET nom = ?, ville = ?, email = ?, description = ? WHERE idUniversity = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, university.getNom());
            stmt.setString(2, university.getVille());
            stmt.setString(3, university.getEmail());
            stmt.setString(4, university.getDescription());
            stmt.setInt(5, university.getIdUniversity());
            int rowsUpdated = stmt.executeUpdate();
        }
    }

    public void supprimer(int idUniversity) throws SQLException {
        String query = "DELETE FROM university WHERE idUniversity = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, idUniversity);  // Associate the ID of the university to delete
            int rowsDeleted = stmt.executeUpdate();  // Execute the delete query
            if (rowsDeleted > 0) {
                System.out.println("University deleted successfully.");
            } else {
                System.out.println("No university found with this ID.");
            }
        }
    }

    public University getLastInsertedUniversity() throws SQLException {
        String query = "SELECT * FROM university ORDER BY idUniversity DESC LIMIT 1";//(highest idUniversity) appears first.(most recent)
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return new University(
                        resultSet.getInt("idUniversity"),
                        resultSet.getString("nom"),
                        resultSet.getString("ville"),
                        resultSet.getString("email"),
                        resultSet.getString("description")
                );
            }
        }
        return null;
    }

    public List<University> afficher() throws SQLException {
        List<University> universities = new ArrayList<>();
        String query = "SELECT * FROM university";
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(query)) {//Executes the query (SELECT * FROM university) and stores the result in rs
            while (rs.next()) {//It returns true if there is another row to process
                University university = new University(
                        rs.getInt("idUniversity"),
                        rs.getString("nom"),
                        rs.getString("ville"),
                        rs.getString("email"),
                        rs.getString("description")
                );
                universities.add(university);//store each uni in List<University>
            }
        }
        return universities;
    }
    public List<String> getDistinctVilles() throws SQLException {
        List<String> villes = new ArrayList<>();
        String query = "SELECT DISTINCT ville FROM university";
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                villes.add(rs.getString("ville"));
            }
        }
        return villes;
    }



    public University getByName(String name){//search uni name in db and return the obj if found
        University u = null;//will later hold the University object
        String sql = "SELECT * FROM university WHERE nom = ?";
        try {
            PreparedStatement pst = connection.prepareStatement(sql);
            pst.setString(1, name);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                University u1 = new University();
                u1.setIdUniversity(rs.getInt("idUniversity"));
                u1.setNom(rs.getString("nom"));//name field from the API response is mapped to the nom field
                u1.setEmail(rs.getString("email"));
                u1.setVille(rs.getString("ville"));
                u1.setDescription(rs.getString("description"));
                u=u1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return u;
    }

    public List<University> fetchUniversities(String country) throws IOException, InterruptedException {//specify the country for the university search
        // Build the API URL with the country parameter
        String url = API_URL + "?country=" + country;

        // Create an HTTP client
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        // Send the request and get the response
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Parse the JSON response into a list of maps
        ObjectMapper mapper = new ObjectMapper();
        List<Map<String, Object>> apiResponse = mapper.readValue(response.body(), new TypeReference<List<Map<String, Object>>>() {});

        // Map the API response to your University entity
        List<University> universities = new ArrayList<>();
        for (Map<String, Object> universityData : apiResponse) {
            University university = new University();
            university.setNom((String) universityData.get("name"));
            university.setVille((String) universityData.get("country")); // Use country as ville
            university.setEmail((String) ((List<?>) universityData.get("domains")).get(0)); // Use the first domain as email
            university.setDescription("University in " + universityData.get("country")); // Add a description
            universities.add(university);
        }

        return universities;
    }
}
