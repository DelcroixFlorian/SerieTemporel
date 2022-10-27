package com.SerieTemporel.Service;

import com.SerieTemporel.modele.Evenement;
import com.SerieTemporel.repository.EvenementRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EvenementService {

    @Autowired
    EvenementRepo evenementRepository;

    public long creerEvenement(Evenement event) {
        Evenement evt = evenementRepository.save(event);
        return evt.getId_event();
    }

    public void supprimerEvenement(Evenement event) {
        evenementRepository.delete(event);
    }

    public Evenement getEvenement(long id) {
        return evenementRepository.findById(id).orElse(null);
    }

    public Evenement updateEvenement(Evenement event) {
        Evenement evt = evenementRepository.save(event);
        return evt;
    }
}
