package com.SerieTemporel.Service;

import com.SerieTemporel.exception.ExcecptionNonAutoriseNonDroit;
import com.SerieTemporel.exception.ExceptionFormatObjetInvalide;
import com.SerieTemporel.exception.ExceptionInterne;
import com.SerieTemporel.modele.Evenement;
import com.SerieTemporel.modele.Serie;
import com.SerieTemporel.repository.EvenementRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EvenementService {

    @Autowired
    EvenementRepo evenementRepository;
    @Autowired
    SerieService serieService;

    /**
     * Création d'un évènement en base de données
     * On lie l'évènement à sa série
     * @param event : l'évènement à créer
     * @return un long identifiant l'évènement
     * @throws ExceptionInterne : Si une exception non gérée survient
     * @throws ExceptionFormatObjetInvalide : Si l'identifiant de la série ne correspond pas à une série existante
     */
    public long creerEvenement(Evenement event, long id_user) throws ExceptionInterne, ExceptionFormatObjetInvalide, ExcecptionNonAutoriseNonDroit {
        // Récupération de l'id de la série et vérification
        long id_serie = event.getId_serie();
        if (serieService.serie_existe(id_serie)) {
            Serie serie_concerne = serieService.get_info_serie(id_serie, id_user);
            try {
                // Création de l'évènement
                Evenement evt = evenementRepository.save(event);
                long id_evt = evt.getId_event();

                // Ajout de l'évènement à la liste de sa série

                serieService.mettre_a_ajour_liste(serie_concerne, event);
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
     * @param id : identifiant de l'évènement à supprimer
     * @throws ExceptionInterne : Si une exception non gérée survient
     * @throws ExceptionFormatObjetInvalide : Si l'évènement à supprimer n'existe pas
     */
    public void supprimerEvenement(long id, long id_user) throws ExceptionFormatObjetInvalide, ExceptionInterne, ExcecptionNonAutoriseNonDroit {
        if (!evenementRepository.existsById(id)) {
            throw new ExceptionFormatObjetInvalide("Erreur, l'événement n'existe pas, suppression impossible.");
        }
        Evenement event = getEvenement(id, id_user);
        serieService.autoriser_serie(event.getId_serie(), id_user, Serie.DROIT_MODIFICATION);

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
     * @throws ExceptionInterne : Si une exception non gérée survient
     */
    public Evenement getEvenement(long id, long id_user) throws ExceptionInterne, ExceptionFormatObjetInvalide, ExcecptionNonAutoriseNonDroit {
        if (!evenementRepository.existsById(id)) {
            throw new ExceptionFormatObjetInvalide("Identifiant de l'évènement incorrect");
        }
        Evenement event = evenementRepository.findById(id).orElse(null);
        assert event != null;
        serieService.autoriser_serie(event.getId_serie(), id_user, Serie.DROIT_CONSULTATION);

        return event;
    }


    /**
     * Mise à jour d'un événement
     * @param event : l'évenement à mettre à jour (contenant toutes les données)
     * @return le nouvel événement mis à jour
     * @throws ExceptionInterne : Si une exception non gérée survient
     * @throws ExceptionFormatObjetInvalide : Si l'évènement n'existe pas
     */
    public Evenement updateEvenement(Evenement event, long id_user) throws ExceptionFormatObjetInvalide, ExceptionInterne, ExcecptionNonAutoriseNonDroit {
        if (!evenementRepository.existsById(event.getId_event())) {
            throw new ExceptionFormatObjetInvalide("Erreur, l'événement n'exite pas, mise à jour impossible.");
        }

        serieService.autoriser_serie(event.getId_serie(), id_user, Serie.DROIT_MODIFICATION);

        try {
            return evenementRepository.save(event);
        } catch (Exception err) {
            throw new ExceptionInterne("erreur de mise à jour");
        }

    }
}
