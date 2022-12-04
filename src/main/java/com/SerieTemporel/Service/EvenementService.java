package com.SerieTemporel.Service;

import com.SerieTemporel.exception.ExceptionEntiteNonTrouvee;
import com.SerieTemporel.exception.ExceptionNonAutoriseNonDroit;
import com.SerieTemporel.exception.ExceptionArgumentIncorrect;
import com.SerieTemporel.exception.ExceptionInterne;
import com.SerieTemporel.modele.Evenement;
import com.SerieTemporel.modele.Serie;
import com.SerieTemporel.repository.EvenementRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.*;

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
     * @param id_user : l'identifiant de l'utilisateur qui initie la création
     * @return un long identifiant l'évènement
     * @throws ExceptionInterne : Si une exception non gérée survient
     * @throws ExceptionArgumentIncorrect : Si l'identifiant de la série ne correspond pas à une série existante
     * @throws ExceptionNonAutoriseNonDroit : Si l'utilisateur n'a pas les droits sur la série ou il veut ajouter un évènement
     * @throws ExceptionEntiteNonTrouvee : si on ne trouve pas l'utilisateur ou la série associée
     */
    @Caching(evict = {
            @CacheEvict(value="utilisateur", key="#id_user"),
            @CacheEvict(value="evenement", key="#event.id_event"),
            @CacheEvict(value="evenements", allEntries = true),
            @CacheEvict(value="evenement_recent", allEntries = true),
            @CacheEvict(value="evenement_nb", allEntries = true)
    })
    public long creerEvenement(Evenement event, long id_user) throws ExceptionInterne, ExceptionArgumentIncorrect, ExceptionNonAutoriseNonDroit, ExceptionEntiteNonTrouvee {
        if (!event.verifier_evenement()) {
            throw new ExceptionArgumentIncorrect("L'étiquette ne doit pas être vide, le numéro de série est un nombre positif.");
        }

        // Récupération de l'id de la série et vérification des droits
        long id_serie = event.getIdSerie();
        if (serieService.serie_existe(id_serie)) {
            Serie serie_concerne = serieService.get_info_serie(id_serie, id_user);
            serieService.autoriser_serie(id_serie, id_user, Serie.DROIT_MODIFICATION);

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
            throw new ExceptionEntiteNonTrouvee(Serie.NOM_ENTITE, id_serie, "Création abandonnée.");
        }
    }


    /**
     * Suppresion d'un événement
     * @param id : identifiant de l'évènement à supprimer
     * @param id_user : l'identifiant de l'utilisateur qui initie la suppression
     * @throws ExceptionInterne : Si une exception non gérée survient
     * @throws ExceptionArgumentIncorrect : Si l'évènement à supprimer n'existe pas
     * @throws ExceptionNonAutoriseNonDroit : Si l'utilisateur n'a pas les droits sur la série ou il veut supprimer un évènement
     */
    @Caching(evict = {
            @CacheEvict(value="utilisateur", key="#id_user"),
            @CacheEvict(value="evenement", key="#id"),
            @CacheEvict(value="evenements", allEntries = true),
            @CacheEvict(value="evenement_recent", allEntries = true),
            @CacheEvict(value="evenement_nb", allEntries = true)
    })
    public void supprimerEvenement(long id, long id_user) throws ExceptionArgumentIncorrect, ExceptionInterne, ExceptionNonAutoriseNonDroit, ExceptionEntiteNonTrouvee {
        if (!evenementRepository.existsById(id)) {
            throw new ExceptionEntiteNonTrouvee(Evenement.NOM_ENTITE, id, "L'événement n'existe pas, suppression impossible.");
        }

        Evenement event = getEvenement(id, id_user);
        serieService.autoriser_serie(event.getIdSerie(), id_user, Serie.DROIT_MODIFICATION);

        try {
            evenementRepository.delete(event);
        } catch (Exception err) {
            throw new ExceptionInterne("erreur de suppression");
        }
    }


    /**
     * Récupération d'un événement dans la base de données
     * @param id : identifiant de l'événement à récupérer
     * @param id_user : l'identifiant de l'utilisateur qui initie la récupération
     * @return l'événement ou null si non existant
     * @throws ExceptionInterne : Si une exception non gérée survient
     * @throws ExceptionArgumentIncorrect : si l'identifiant ne correspond à rien
     * @throws ExceptionNonAutoriseNonDroit : Si l'utilisateur n'a pas les droits sur la série ou il veut consulter un évènement
     */
    @Cacheable(value="evenement", key="#id")
    public Evenement getEvenement(long id, long id_user) throws ExceptionInterne, ExceptionArgumentIncorrect, ExceptionNonAutoriseNonDroit, ExceptionEntiteNonTrouvee {
        if (!evenementRepository.existsById(id)) {
            throw new ExceptionEntiteNonTrouvee(Evenement.NOM_ENTITE, id, "Identifiant de l'évènement incorrect");
        }

        Evenement event;
        try {
            event = evenementRepository.findById(id).orElse(null);
        } catch (Exception err) {
            throw new ExceptionInterne("erreur de récupération");
        }

        assert event != null;
        serieService.autoriser_serie(event.getIdSerie(), id_user, Serie.DROIT_CONSULTATION);

        return event;
    }


    /**
     * Retourne tous les événements d'une Série dont l'étiquette est égale au paramètre étiquette
     * @param id_serie : l'identifiant de la série dans laquelle on effectue une recherche
     * @param etiquette : l'étiquette exacte recherchée
     * @param id_user : l'idenfiant de l'utilisateur initiant la requête
     * @return une liste d'évenement correspondant aux critères
     * @throws ExceptionEntiteNonTrouvee : si la série existe pas
     */
    @Cacheable(value = "evenements", key="#id_serie+#etiquette")
    public Iterable<Evenement> getEvenementsEtiquetteSerie(long id_serie, String etiquette, long id_user)
            throws ExceptionInterne, ExceptionNonAutoriseNonDroit, ExceptionEntiteNonTrouvee {

        if (!serieService.serie_existe(id_serie)) {
            throw new ExceptionEntiteNonTrouvee(Serie.NOM_ENTITE, id_serie, "Identifiant de la série incorrect");
        }

        serieService.autoriser_serie(id_serie, id_user, Serie.DROIT_CONSULTATION);

        try {
            return evenementRepository.getEvenementsByEtiquetteAndIdSerie(etiquette, id_serie);

        } catch (Exception err) {
            throw new ExceptionInterne("erreur de récupération");
        }
    }


    /**
     * Retourne l'évènement de l'étiquette et la série spécifié dont la date est la plus récente
     * @param id_serie : l'identifiant de la série dans laquelle on effectue une recherche
     * @param etiquette : l'étiquette exacte recherchée
     * @param id_user : l'idenfiant de l'utilisateur initiant la requête
     * @return un événement
     * @throws ExceptionEntiteNonTrouvee : si la série existe pas
     */
    @Cacheable(value = "evenement_recent", key="#id_serie+#etiquette")
    public Iterable<Evenement> getEvenementEtiquetteSerieRecent(long id_serie, String etiquette, long id_user)
            throws ExceptionInterne, ExceptionNonAutoriseNonDroit, ExceptionEntiteNonTrouvee {

        if (!serieService.serie_existe(id_serie)) {
            throw new ExceptionEntiteNonTrouvee(Serie.NOM_ENTITE, id_serie, "Identifiant de la série incorrect");
        }

        serieService.autoriser_serie(id_serie, id_user, Serie.DROIT_CONSULTATION);

        try {
            return evenementRepository.getEvenementsByEtiquetteAndIdSerieOrderByDateDesc(etiquette, id_serie);
        } catch (Exception err) {
            throw new ExceptionInterne("erreur de récupération");
        }
    }


    /**
     * Retourne le nombre de jour depuis la dernière occurence d'un événement
     * @param id_serie : l'identifiant de la série dans laquelle on effectue une recherche
     * @param etiquette : l'étiquette exacte recherchée
     * @param id_user : l'idenfiant de l'utilisateur initiant la requête
     * @return long : le nombre de jour écoulé entre la date de l'évènement et aujourd'hui
     */
    public long delai_en_jour_ecart(Long id_serie, String etiquette, Long id_user)
            throws ExceptionNonAutoriseNonDroit, ExceptionInterne, ExceptionEntiteNonTrouvee {

        Iterable<Evenement> iter = getEvenementEtiquetteSerieRecent(id_serie, etiquette, id_user);
        ArrayList<Evenement> alitst = new ArrayList<>();
        for (Evenement evt: iter) {
            alitst.add(evt);
            break;
        }

        Evenement event = alitst.get(0);
        java.util.Date  utilDate = new java.util.Date(event.getDate().getTime());
        Duration duration = Duration.between(utilDate.toInstant(), Instant.now());

        return duration.toDays();
    }

    /**
     * Mise à jour d'un événement
     * @param event : l'évenement à mettre à jour (contenant toutes les données)
     * @param id_user : l'identifiant de l'utilisateur qui initie la création
     * @return le nouvel événement mis à jour
     * @throws ExceptionInterne : Si une exception non gérée survient
     * @throws ExceptionArgumentIncorrect : Si l'évènement n'existe pas
     * @throws ExceptionNonAutoriseNonDroit : Si l'utilisateur n'a pas les droits sur la série ou il veut mettre à jour un évènement
     */
    @Caching(evict = {
            @CacheEvict(value="utilisateur", key="#id_user"),
            @CacheEvict(value="evenement", key="#event.id_event"),
            @CacheEvict(value="evenements", allEntries = true),
            @CacheEvict(value="evenement_recent", allEntries = true),
            @CacheEvict(value="evenement_nb", allEntries = true)
    })
    public Evenement updateEvenement(Evenement event, long id_user)
            throws ExceptionArgumentIncorrect, ExceptionInterne, ExceptionNonAutoriseNonDroit, ExceptionEntiteNonTrouvee {

        if (!evenementRepository.existsById(event.getId_event())) {
            throw new ExceptionEntiteNonTrouvee(Evenement.NOM_ENTITE, event.getId_event(), "Erreur, l'événement n'exite pas, mise à jour impossible.");
        }

        serieService.autoriser_serie(event.getIdSerie(), id_user, Serie.DROIT_MODIFICATION);

        try {
            return evenementRepository.save(event);
        } catch (Exception err) {
            throw new ExceptionInterne("erreur de mise à jour");
        }

    }


    /**
     *
     * @param etiquette
     * @param id_serie
     * @param date_debut
     * @param date_fin
     * @param id_user
     * @return
     * @throws ExceptionEntiteNonTrouvee
     * @throws ExceptionNonAutoriseNonDroit
     * @throws ExceptionInterne
     */
    @Cacheable(value = "evenement_nb", key = "#id_serie+#etiquette+#date_debut+#date_fin")
    public int get_nombre_evenement_entre_deux_date(String etiquette, Long id_serie, Date date_debut, Date date_fin, Long id_user) throws ExceptionEntiteNonTrouvee, ExceptionNonAutoriseNonDroit, ExceptionInterne {
        if (!serieService.serie_existe(id_serie)) {
            throw new ExceptionEntiteNonTrouvee(Serie.NOM_ENTITE, id_serie, "Identifiant de la série incorrect");
        }

        serieService.autoriser_serie(id_serie, id_user, Serie.DROIT_CONSULTATION);

        try {
            return evenementRepository.countEvenementByEtiquetteAndIdSerieAndDateBetween(etiquette, id_serie, date_debut, date_fin);
        } catch (Exception err) {
            throw new ExceptionInterne("erreur de récupération");
        }
    }


    /**
     * Transforme une liste d'événement en une map de couple Date/valeur
     * @param liste_event : liste des événements à ajouter à la HasHMap
     * @return Une HashMap de couple Date/Valeur
     */
    public Map<String, Double> get_liste_couple_valeur_date(Iterable<Evenement> liste_event) {
        Map<String, Double> liste_couple = new HashMap<>();
        for (Evenement evt : liste_event) {
            liste_couple.put(evt.getId_event() + " : " + evt.getDate().toLocalDate(), evt.getValeur());
        }

        return liste_couple;
    }
}
