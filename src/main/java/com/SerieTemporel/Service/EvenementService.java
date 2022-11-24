package com.SerieTemporel.Service;

import com.SerieTemporel.modele.Evenement;
import com.SerieTemporel.modele.Serie;
import com.SerieTemporel.repository.EvenementRepo;
import com.SerieTemporel.repository.SerieRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/*
 *
 */
@Service
public class EvenementService {

    @Autowired
    EvenementRepo evenementRepository;
    @Autowired
    SerieService serieService;
    @Autowired
    SerieRepo serieRepo;

    /**
     * Création d'un évenement en base de données
     * On lie l'événement à sa série
     * @param event : l'événement à créer
     * @return l'identifiant de l'événement
     *         -1 si la série de l'événement n'existe pas
     */
    public long creerEvenement(Evenement event) throws ExceptionInterne, ExceptionFormatObjetInvalide {
        // Vérification
        long id_serie = event.getId_serie();

        Serie serie_concerne = serieService.get_info_serie(id_serie);
        if (serie_concerne != null) {
            try {
                Evenement evt = evenementRepository.save(event);
                long id_evt = evt.getId_event();
                serie_concerne.ajouter_evenement_liste(id_evt);
                serieRepo.save(serie_concerne);
                return id_evt;
            } catch (Exception err) {
                throw new ExceptionInterne("erreur creation");
            }
        } else {
            throw new ExceptionFormatObjetInvalide("L'identifiant de la série n'a pas pu être identifié. Création abandonnée.");
        }
    }


    /**
     * Suppresion d'un événement
     * @param id : identifiant de l'événement à supprimer
     */
    public void supprimerEvenement(long id) throws ExceptionFormatObjetInvalide, ExceptionInterne {
        Evenement event = getEvenement(id);
        if (event == null) {
            throw new ExceptionFormatObjetInvalide("Erreur, l'événement n'existe pas, suppression impossible.");
        }
        try {
            evenementRepository.delete(event);
        } catch (Exception err) {
            throw new ExceptionInterne("erreur de suppression");
        }
    }


    /**
     * Récupération d'un événement dans la base de données
     * @param id : identifiant de l'événement à récupérer
     * @return l'événement ou null si non existant
     */
    public Evenement getEvenement(long id) throws ExceptionInterne {
        try {
            return evenementRepository.findById(id).orElse(null);
        } catch (Exception err) {
            throw new ExceptionInterne("erreur de récupération de l'événement");
        }

    }


    /**
     * Mise à jour d'un événement
     * @param event : l'évenement à mettre à jour (contenant toutes les données)
     * @return le nouvel événement mis à jour
     */
    public Evenement updateEvenement(Evenement event) throws ExceptionFormatObjetInvalide, ExceptionInterne {
        if (getEvenement(event.getId_event()) == null) {
            throw new ExceptionFormatObjetInvalide("Erreur, l'événement n'exite pas, mise à jour impossible.");
        }
        try {
            return evenementRepository.save(event);
        } catch (Exception err) {
            throw new ExceptionInterne("erreur de mise à jour");
        }

    }
}
