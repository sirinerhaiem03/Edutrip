package tn.EduTrip.tests;

import tn.EduTrip.entites.ReserVol;
import tn.EduTrip.entites.Vol;
import tn.EduTrip.services.APIService;
import tn.EduTrip.services.ServiceReserVol;
import tn.EduTrip.services.ServiceVol;

import java.sql.SQLException;
import java.sql.Timestamp;

import java.text.SimpleDateFormat;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        APIService apiService = new APIService();
        ServiceVol serviceVol = new ServiceVol();

        try {
            List<Vol> vols = apiService.recupererVolsDepuisAPI();
            serviceVol.ajouterVolsDepuisAPI(vols);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
