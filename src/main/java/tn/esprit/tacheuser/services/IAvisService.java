package tn.esprit.tacheuser.services;

import tn.esprit.tacheuser.models.Avis;
import java.util.List;

public interface IAvisService {
    void addAvis(Avis avis);
    void deleteAvis(int avisId);
    void updateAvis(Avis avis);
    List<Avis> getAllAvis();
}
