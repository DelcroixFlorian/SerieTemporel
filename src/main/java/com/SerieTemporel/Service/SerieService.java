package com.SerieTemporel.Service;

import com.SerieTemporel.exception.ExceptionEntiteNonTrouvee;
import com.SerieTemporel.exception.ExceptionNonAutoriseNonDroit;
import com.SerieTemporel.exception.ExceptionArgumentIncorrect;
import com.SerieTemporel.exception.ExceptionInterne;
import com.SerieTemporel.modele.Evenement;
import com.SerieTemporel.modele.Serie;
import com.SerieTemporel.modele.Utilisateur;
import com.SerieTemporel.repository.SerieRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
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
     * @throws ExceptionArgumentIncorrect Si l'utilisateur qui veut créer la série n'existe pas
     */
    @CacheEvict(value="utilisateur", key="#new_serie.id_user")
    public long creer_serie(Serie new_serie) throws ExceptionInterne, ExceptionArgumentIncorrect, ExceptionEntiteNonTrouvee {
        utilisateurService.verifier_existe(new_serie);

        if (!new_serie.verifier_argument()) {
            throw new ExceptionArgumentIncorrect("Le titre de la série et sa description ne doivent pas être vides.");
        }

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
     * @throws ExceptionArgumentIncorrect : Si la série n'existe pas
     * @throws ExceptionNonAutoriseNonDroit : Si l'utilisateur n'est pas propriétaire ou si il n'a pas de partage
     */
    @Cacheable(value = "serie", key = "#id")
    public Serie get_info_serie(long id, long id_user) throws ExceptionInterne, ExceptionArgumentIncorrect, ExceptionNonAutoriseNonDroit, ExceptionEntiteNonTrouvee {
        if (!serieRepo.existsById(id)) {
            throw new ExceptionEntiteNonTrouvee(Serie.NOM_ENTITE,id, "Identifiant de la série incorrect.");
        }

        Serie serie = serieRepo.findById(id).orElse(null);
        if (serie != null) {
            utilisateurService.verifier_existe(serie);

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
     * @throws ExceptionNonAutoriseNonDroit : Si les droits sont insuffisants (ou inexistant)
     * @throws ExceptionEntiteNonTrouvee : si une entié n'est pas trouvée
     */
    public void autoriser_serie(long id_serie, long id_user, String droit) throws ExceptionInterne, ExceptionNonAutoriseNonDroit, ExceptionEntiteNonTrouvee {
        if (!serieRepo.existsById(id_serie)) {
            throw new ExceptionEntiteNonTrouvee(Serie.NOM_ENTITE,id_serie, "Identifiant de la série incorrect.");
        }

        Serie serie = serieRepo.findById(id_serie).orElse(null);
        if (serie != null) {
            utilisateurService.verifier_existe(serie);

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
    @Caching(evict = {
            @CacheEvict(value="utilisateur", key="#serie.id_user"),
            @CacheEvict(value ="serie", key = "#serie.getId()")
    })
    public void mettre_a_ajour_liste(Serie serie, Evenement event) {
        serie.ajouter_evenement_liste(event);
        serieRepo.save(serie);
    }


    /**
     * Suppression d'une Serie
     * @param id : identifiant de la serie à supprimer
     * @param id_user : identifiant de l'utilisateur initiant la suppresion
     * @throws ExceptionArgumentIncorrect : Si la serie n'existe pas
     * @throws ExceptionInterne : si la suppresion échoue
     * @throws ExceptionNonAutoriseNonDroit : si l'utilisateur n'a pas les droits d'accés à la serie
     */
    @Caching(evict = {
            @CacheEvict(value="utilisateur", key="#id_user"),
            @CacheEvict(value ="serie", key = "#id")
    })
    public void supprimer_serie(long id, long id_user) throws ExceptionArgumentIncorrect, ExceptionInterne, ExceptionNonAutoriseNonDroit, ExceptionEntiteNonTrouvee {
        if (!serieRepo.existsById(id)) {
            throw new ExceptionEntiteNonTrouvee(Serie.NOM_ENTITE, id, "Erreur, la série n'existe pas, suppression impossible.");
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
     * @throws ExceptionArgumentIncorrect : Si on demande des droits inconnus
     * @throws ExceptionNonAutoriseNonDroit : Si on a pas les droits nécessaires pour partager la serie
     */
    @Caching(evict = {
            @CacheEvict(value="utilisateur", key="#id_user"),
            @CacheEvict(value ="serie", key = "#id_serie")
    })
    public void partager_serie(long id_user_a_partager, long id_serie, String droit, long id_user) throws ExceptionInterne, ExceptionArgumentIncorrect, ExceptionNonAutoriseNonDroit, ExceptionEntiteNonTrouvee {
        Utilisateur user_partager = utilisateurService.getUtilisateur(id_user_a_partager);

        if (user_partager == null) {
            throw new ExceptionInterne("Erreur de récupération de l'utilisateur");
        }

        Serie serie_a_partager = get_info_serie(id_serie, id_user);

        if (serie_a_partager == null) {
            throw new ExceptionInterne("Erreur de récupération de la série");
        }

        serie_a_partager.ajouter_partage(user_partager, droit);
        serieRepo.save(serie_a_partager);
    }


    /**
     * Modifie le partage d'une serie
     * @param id_user_a_partager : identifiant de l'utilisateur à qui on souhaite modifier les droits
     * @param id_serie : identifiant de la serie partagée
     * @param droit : droit à octroyer à travers ce partage
     * @param id_user : idenfifiant de l'utilisateur initiant le partage
     * @throws ExceptionInterne : Si on arrive pas récupérer les différentes entités
     * @throws ExceptionArgumentIncorrect : Si on demande des droits inconnus
     * @throws ExceptionNonAutoriseNonDroit : Si on a pas les droits nécessaires pour partager la serie
     */
    @Caching(evict = {
            @CacheEvict(value="utilisateur", key="#id_user"),
            @CacheEvict(value ="serie", key = "#id_serie")
    })
    public void modifier_partage_serie(long id_user_a_partager, long id_serie, long id_user, String droit) throws ExceptionInterne, ExceptionArgumentIncorrect, ExceptionNonAutoriseNonDroit, ExceptionEntiteNonTrouvee {
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
     * @throws ExceptionArgumentIncorrect :
     * @throws ExceptionNonAutoriseNonDroit : Si on a pas les droits nécessaires pour supprimer le partage de la serie
     */
    @Caching(evict = {
            @CacheEvict(value="utilisateur", key="#id_user"),
            @CacheEvict(value ="serie", key = "#id_serie")
    })
    public void supprimer_partage_serie(long id_user_a_partager, long id_serie, long id_user) throws ExceptionInterne, ExceptionArgumentIncorrect, ExceptionNonAutoriseNonDroit, ExceptionEntiteNonTrouvee {
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
