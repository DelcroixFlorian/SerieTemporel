package com.SerieTemporel.Service;

import com.SerieTemporel.modele.Evenement;
import com.SerieTemporel.modele.Serie;
import com.SerieTemporel.repository.EvenementRepo;
import com.SerieTemporel.repository.SerieRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EvenementService {

    @Autowired
    EvenementRepo evenementRepository;
    @Autowired
    SerieService serieService;
    @Autowired
    SerieRepo serieRepo;

    public long creerEvenement(Evenement event) {
        // Vérification
        long id_serie = event.getId_serie();

        Serie serie_concerne = serieService.get_info_serie(id_serie);
        if (serie_concerne != null) {
            Evenement evt = evenementRepository.save(event);
            long id_evt = evt.getId_event();
            serie_concerne.ajouter_evenement_liste(id_evt);
            serieRepo.save(serie_concerne);
            return id_evt;
        } else {
            return -1;
        }
    }

    public void supprimerEvenement(Evenement event) {
        evenementRepository.delete(event);
    }

    public Evenement getEvenement(long id) {
        return evenementRepository.findById(id).orElse(null);
    }

    public Evenement updateEvenement(Evenement event) {
        return evenementRepository.save(event);
    }
}
