package com.SerieTemporel.Service;

import com.SerieTemporel.exception.ExceptionNonAutoriseNonDroit;
import com.SerieTemporel.exception.ExceptionFormatObjetInvalide;
import com.SerieTemporel.exception.ExceptionInterne;
import com.SerieTemporel.modele.Evenement;
import com.SerieTemporel.modele.Serie;
import com.SerieTemporel.modele.Utilisateur;
import com.SerieTemporel.repository.SerieRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
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
     * @throws ExceptionFormatObjetInvalide Si l'utilisateur qui veut créer la série n'existe pas
     */
    @CacheEvict(value="utilisateur", key="#new_serie.id_user")
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
     * @param id_user : long identifiant de l'utilisateur qui initie la requete
     * @return la série si elle existe
     * @throws ExceptionInterne : Si on rencontre une erreur lors de la récupération
     * @throws ExceptionFormatObjetInvalide : Si la série n'existe pas
     * @throws ExceptionNonAutoriseNonDroit : Si l'utilisateur n'est pas propriétaire ou si il n'a pas de partage
     */
    @Cacheable("serie")
    public Serie get_info_serie(long id, long id_user) throws ExceptionInterne, ExceptionFormatObjetInvalide, ExceptionNonAutoriseNonDroit {
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
                throw new ExceptionNonAutoriseNonDroit("Vous n'avez pas le droit d'accéder à cette série !");
            }
        } else {
            throw new ExceptionInterne("erreur de récupération de la série");
        }
    }


    /**
     * Vérifier les autorisations nécessaires relatives à une Serie
     * @param id_serie : identifiant de la Serie concerne
     * @param id_user : identifiant de l'utilisateur qui tente d'accéder à la Serie
     * @param droit : droit nécessaire pour réaliser l'action pour laquelle on vérifie les autorisations
     * @throws ExceptionInterne : Si on arrive pas à récupérer la série correspondant à l'identifiant
     * @throws ExceptionFormatObjetInvalide : Si le numéro de la Serie n'existe pas
     * @throws ExceptionNonAutoriseNonDroit : Si les droits sont insuffisants (ou inexistant)
     */
    public void autoriser_serie(long id_serie, long id_user, String droit) throws ExceptionInterne, ExceptionFormatObjetInvalide, ExceptionNonAutoriseNonDroit {
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
                throw new ExceptionNonAutoriseNonDroit("Vous n'avez pas le droit d'accéder à cette série !");
            }
        } else {
            throw new ExceptionInterne("erreur de récupération de la série");
        }
    }


    /**
     * Statue sur l'existence d'une série dans la base de données en foncion de son identifiant
     * @param id_serie : long, identifiant de la série dont on cherche à vérifier l'existence
     * @return True si la serie exixte en base de données
     */
    public boolean serie_existe(Long id_serie) {
        return serieRepo.existsById(id_serie);
    }

    /**
     * Ajoute un évènement à la serie
     * @param serie : serie sur laquelle on veut ajouter un événement
     * @param event : événement à ajouter à la serie
     */
    @CacheEvict(value="utilisateur", key="#serie.id_user")
    public void mettre_a_ajour_liste(Serie serie, Evenement event) {
        serie.ajouter_evenement_liste(event);
        serieRepo.save(serie);
    }


    /**
     * Suppression d'une Serie
     * @param id : identifiant de la serie à supprimer
     * @param id_user : identifiant de l'utilisateur initiant la suppresion
     * @throws ExceptionFormatObjetInvalide : Si la serie n'existe pas
     * @throws ExceptionInterne : si la suppresion échoue
     * @throws ExceptionNonAutoriseNonDroit : si l'utilisateur n'a pas les droits d'accés à la serie
     */
    @CacheEvict(value="utilisateur", key="#id_user")
    public void supprimer_serie(long id, long id_user) throws ExceptionFormatObjetInvalide, ExceptionInterne, ExceptionNonAutoriseNonDroit {
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


    /**
     * Partage d'une serie a un nouvel utilisateur
     * @param id_user_a_partager : identifiant de l'utilisateur à qui on souhaite donner les droits
     * @param id_serie : identifiant de la serie qu'on souhaite partager
     * @param droit : droit à octroyer à travers ce partage
     * @param id_user : idenfifiant de l'utilisateur initiant le partage
     * @throws ExceptionInterne : Si on arrive pas récupérer les différentes entités
     * @throws ExceptionFormatObjetInvalide : Si on demande des droits inconnus
     * @throws ExceptionNonAutoriseNonDroit : Si on a pas les droits nécessaires pour partager la serie
     */
    @CacheEvict(value="utilisateur", key="#id_user")
    public void partager_serie(long id_user_a_partager, long id_serie, int droit, long id_user) throws ExceptionInterne, ExceptionFormatObjetInvalide, ExceptionNonAutoriseNonDroit {
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


    /**
     * Modifie le partage d'une serie
     * @param id_user_a_partager : identifiant de l'utilisateur à qui on souhaite modifier les droits
     * @param id_serie : identifiant de la serie partagée
     * @param droit : droit à octroyer à travers ce partage
     * @param id_user : idenfifiant de l'utilisateur initiant le partage
     * @throws ExceptionInterne : Si on arrive pas récupérer les différentes entités
     * @throws ExceptionFormatObjetInvalide : Si on demande des droits inconnus
     * @throws ExceptionNonAutoriseNonDroit : Si on a pas les droits nécessaires pour partager la serie
     */
    @CacheEvict(value="utilisateur", key="#id_user")
    public void modifier_partage_serie(long id_user_a_partager, long id_serie, long id_user, String droit) throws ExceptionInterne, ExceptionFormatObjetInvalide, ExceptionNonAutoriseNonDroit {
        Utilisateur user_partager = utilisateurService.getUtilisateur(id_user_a_partager);

        if (user_partager == null) {
            throw new ExceptionInterne("Erreur de récupération de l'utilisateur");
        }

        Serie serie_a_partager = get_info_serie(id_serie, id_user);

        if (serie_a_partager == null) {
            throw new ExceptionInterne("Erreur de récupération de la série");
        }

        serie_a_partager.modifier_partage(user_partager, droit);
        serieRepo.save(serie_a_partager);
    }

    /**
     * Supprime le partage d'une serie
     * @param id_user_a_partager : identifiant de l'utilisateur à qui on souhaite supprimer les droits
     * @param id_serie : identifiant de la serie partagée
     * @param id_user : idenfifiant de l'utilisateur initiant le partage
     * @throws ExceptionInterne : Si on arrive pas récupérer les différentes entités
     * @throws ExceptionFormatObjetInvalide :
     * @throws ExceptionNonAutoriseNonDroit : Si on a pas les droits nécessaires pour supprimer le partage de la serie
     */
    @CacheEvict(value="utilisateur", key="#id_user")
    public void supprimer_partage_serie(long id_user_a_partager, long id_serie, long id_user) throws ExceptionInterne, ExceptionFormatObjetInvalide, ExceptionNonAutoriseNonDroit {
        Utilisateur user_partager = utilisateurService.getUtilisateur(id_user_a_partager);

        if (user_partager == null) {
            throw new ExceptionInterne("Erreur de récupération de l'utilisateur");
        }

        Serie serie_a_partager = get_info_serie(id_serie, id_user);

        if (serie_a_partager == null) {
            throw new ExceptionInterne("Erreur de récupération de la série");
        }

        serie_a_partager.supprimer_partage(user_partager);
        serieRepo.save(serie_a_partager);
    }

}
