package com.SerieTemporel.Service;

import com.SerieTemporel.exception.ExcecptionNonAutoriseNonDroit;
import com.SerieTemporel.exception.ExceptionFormatObjetInvalide;
import com.SerieTemporel.exception.ExceptionInterne;
import com.SerieTemporel.modele.Evenement;
import com.SerieTemporel.modele.Serie;
import com.SerieTemporel.modele.Utilisateur;
import com.SerieTemporel.repository.SerieRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class SerieService {

    @Autowired
    SerieRepo serieRepo;

    @Autowired
    UtilisateurService utilisateurService;

    /**
     * Créer une série
     * @param new_serie Série à insérer dans la base de données
     * @return long : l'identifiant de la série créée
     * @throws ExceptionInterne Si le serveur échoue à créer la série
     */
    public long creer_serie(Serie new_serie) throws ExceptionInterne, ExceptionFormatObjetInvalide {

        utilisateurService.verifier_existe(new_serie);

        try {
            Serie serie =  serieRepo.save(new_serie);
            utilisateurService.ajouter_serie( serie);
            return serie.getId();
        } catch (Exception err) {
            throw new ExceptionInterne("erreur de création de la série");
        }
    }


    /**
     * Récupère les données d'une série
     * @param id : long identifiant de la série
     * @return la série si elle existe
     * @throws ExceptionInterne : Si on rencontre une erreur lors de la récupération
     * @throws ExceptionFormatObjetInvalide : Si la série n'existe pas
     */
    public Serie get_info_serie(long id, long id_user) throws ExceptionInterne, ExceptionFormatObjetInvalide, ExcecptionNonAutoriseNonDroit {
        if (!serieRepo.existsById(id)) {
            throw new ExceptionFormatObjetInvalide("Identifiant de la série incorrect.");
        }

        Serie serie = serieRepo.findById(id).orElse(null);
        if (serie != null) {
            List<Long> liste_user = serie.getListe_utilisateur_partagee();
            if (id_user == serie.getId_user() || liste_user.contains(id_user)) {
                // Autoriser
                return serie;
            } else {
                throw new ExcecptionNonAutoriseNonDroit("Vous n'avez pas le droit d'accéder à cette série !");
            }
        } else {
            throw new ExceptionInterne("erreur de récupération de la série");
        }
    }

    public void autoriser_serie(long id_serie, long id_user, String droit) throws ExceptionInterne, ExceptionFormatObjetInvalide, ExcecptionNonAutoriseNonDroit {
        if (!serieRepo.existsById(id_serie)) {
            throw new ExceptionFormatObjetInvalide("Identifiant de la série incorrect.");
        }

        Serie serie = serieRepo.findById(id_serie).orElse(null);
        if (serie != null) {
            List<Long> liste_user = serie.getListe_utilisateur_partagee();
            int index = liste_user.indexOf(id_user);
            if ( (id_user != serie.getId_user() // Non propriétaire
                  && index == -1) // Non partagé
                || id_user != serie.getId_user() && index != -1 && !serie.getListe_droit_serie_partagee().get(index).equals(droit)) // Partagé mais droits insuffisants
            {
                throw new ExcecptionNonAutoriseNonDroit("Vous n'avez pas le droit d'accéder à cette série !");
            }
        } else {
            throw new ExceptionInterne("erreur de récupération de la série");
        }
    }

    public boolean serie_existe(Long id_serie) {
        return serieRepo.existsById(id_serie);
    }

    public void mettre_a_ajour_liste(Serie serie, Evenement event) {

        serie.ajouter_evenement_liste(event);
        serieRepo.save(serie);
    }


    public void supprimer_serie(long id, long id_user) throws ExceptionFormatObjetInvalide, ExceptionInterne, ExcecptionNonAutoriseNonDroit {
        if (!serieRepo.existsById(id)) {
            throw new ExceptionFormatObjetInvalide("Erreur, la série n'existe pas, suppression impossible.");
        }

        Serie serie = get_info_serie(id, id_user);

        try {
            serieRepo.delete(serie);
        } catch (Exception err) {
            throw new ExceptionInterne("erreur de suppression");
        }
    }

    public void partager_serie(long id_user_a_partager, long id_serie, int droit, long id_user) throws ExceptionInterne, ExceptionFormatObjetInvalide, ExcecptionNonAutoriseNonDroit {
        Utilisateur user_partager = utilisateurService.getUtilisateur(id_user_a_partager);

        if (user_partager == null) {
            throw new ExceptionInterne("Erreur de récupération de l'utilisateur");
        }

        Serie serie_a_partager = get_info_serie(id_serie, id_user);

        if (serie_a_partager == null) {
            throw new ExceptionInterne("Erreur de récupération de la série");
        }

        String droit_str;
        if (droit == 1) {
            droit_str = Serie.DROIT_CONSULTATION;
        } else if (droit == 2) {
            droit_str = Serie.DROIT_MODIFICATION;
        } else {
            throw new ExceptionFormatObjetInvalide("Demande de droit inconnu.");
        }
        serie_a_partager.ajouter_partage(user_partager, droit_str);
        serieRepo.save(serie_a_partager);
    }

}
