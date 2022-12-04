package com.SerieTemporel.controller;

import com.SerieTemporel.Service.EvenementService;
import com.SerieTemporel.exception.ExceptionEntiteNonTrouvee;
import com.SerieTemporel.exception.ExceptionNonAutoriseNonDroit;
import com.SerieTemporel.exception.ExceptionArgumentIncorrect;
import com.SerieTemporel.exception.ExceptionInterne;
import com.SerieTemporel.modele.Evenement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/*
 * Gestion des requêtes concernant les événements
 */
@RestController
public class EvenementController {

    @Autowired
    EvenementService serviceEvenement;
    

    /**
     * Requête POST pour la création d'un événement
     * @param new_evenement une représentation de l'évenement
     *                      ex json : { "id_serie": 1,
     *                                  "date": "2022-10-24",
     *                                  "valeur": 150.0,
     *                                  "etiquette" :  "Escrime",
     *                                  "commentaire" : ""
     *                                 }
     * @return NOT_FOUND si la série associée n'existe pas
     *         CREATED si l'objet a pu être créé, renvoi son identifiant
     *         INTERNAL_SERVER_ERROR si le serveur échoue à créer l'événement
     *         UNAUTHORIZED si l'utilisateur n'a pas les droits de modification sur la série pour ajouter l'évènement
     */
    @PostMapping("/{id_user}/evenement")
    public ResponseEntity ajouter_evenement(@PathVariable long id_user, @RequestBody Evenement new_evenement) {
        // Création de l'élément en base via le service et récupération de son identifiant
        try {
            long id_new_event = serviceEvenement.creerEvenement(new_evenement, id_user);
            // On renvoi l'identifiant du nouvel événement avec 201 comme statut
            return ResponseEntity.status(HttpStatus.CREATED).body("Id de l'évenement : " + id_new_event);

        } catch (ExceptionInterne err) {
            return ResponseEntity.internalServerError().body(err.getMessage());

        } catch (ExceptionArgumentIncorrect err) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err.getMessage());

        } catch (ExceptionNonAutoriseNonDroit err) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(err.getMessage());

        } catch (ExceptionEntiteNonTrouvee err) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err.getMessage());
        }

    }


    /**
     * Requête DELETE pour la suppression d'un événement
     * @param id : identifiant de l'évènement à supprimer
     * @return NOT_FOUND si l'événement n'existe pas
     *         INTERNAL_SERVER_ERROR si la suppression a échoué
     *         NO_CONTENT si tout s'est bien passé
     *         UNAUTHORIZED si l'utilisateur n'a pas les droits de modification sur la série pour supprimer l'évènement
     */
    @DeleteMapping("/{id_user}/evenement/{id}")
    public ResponseEntity supprimer_evenement(@PathVariable long id_user, @PathVariable("id") long id) {
        try {
            serviceEvenement.supprimerEvenement(id, id_user);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

        } catch (ExceptionArgumentIncorrect err) {
            return ResponseEntity.badRequest().body(err.getMessage());

        } catch (ExceptionInterne err) {
            return ResponseEntity.internalServerError().body(err.getMessage());

        } catch (ExceptionNonAutoriseNonDroit err) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(err.getMessage());

        } catch (ExceptionEntiteNonTrouvee err) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err.getMessage());
        }
    }


    /**
     * Requête PUT pour modifier un évènement
     * @param evt : une représentation de l'évènement à modifier
     * @return NOT_FOUND si l'évènement n'existe pas
     *         INTERNAL_SERVER_ERROR si la mise à jour a échouée
     *         CREATED si tout s'est bien passé + nouvel_evt
     *         UNAUTHORIZED si l'utilisateur n'a pas les droits de modification pour modifier à la série de l'évènement
     */
    @PutMapping("/{id_user}/evenement")
    public ResponseEntity update_evenement(@PathVariable long id_user, @RequestBody Evenement evt) {
        try {
            Evenement evt_a_jour = serviceEvenement.updateEvenement(evt, id_user);
            return ResponseEntity.status(HttpStatus.CREATED).body(evt_a_jour);

        } catch (ExceptionArgumentIncorrect err) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err.getMessage());

        } catch (ExceptionInterne err) {
            return ResponseEntity.internalServerError().body(err.getMessage());

        } catch (ExceptionNonAutoriseNonDroit err) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(err.getMessage());

        } catch (ExceptionEntiteNonTrouvee err) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err.getMessage());
        }
    }


    /**
     * Requête GET récupération d'un événement
     * @param id_event : identifiant de l'évènement
     * @param id_user : identifiant unique de l'utilisateur qui demande à voir l'évènement
     * @return OK (200) Si on a pu récupérer l'évènement + representation de l'évènement
     *         INTERNAL_SERVER_ERROR si on échoue à récupérer l'évènement
     *         NOT_FOUND si l'identifiant de l'évènement est incorrect
     *         UNAUTHORIZED si l'utilisateur n'a pas les droits suffisant pour accéder à la série de l'évènement
     */
    @GetMapping("/{id_user}/evenement/{id_event}")
    public ResponseEntity voir_evenement(@PathVariable long id_user, @PathVariable("id_event") long id_event) {
        try {
            Evenement evt = serviceEvenement.getEvenement(id_event, id_user);
            return ResponseEntity.status(HttpStatus.OK).body(evt);

        } catch (ExceptionInterne err) {
            return ResponseEntity.internalServerError().body(err.getMessage());

        } catch (ExceptionArgumentIncorrect err) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err.getMessage());

        } catch (ExceptionNonAutoriseNonDroit err) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(err.getMessage());

        } catch (ExceptionEntiteNonTrouvee err) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err.getMessage());
        }

    }


    /**
     * Retourne une liste d'évènements d'une série en fonction d'une étiquette
     * @param etiquette : Critère de recherche de l'étiquette, recherche exacte (Sensible à la casse)
     * @param id_serie : identifiant de la série dans laquelle on va chercher
     * @param id_user : identifiant unique de l'utilisateur qui demande à voir les évènements
     * @return OK (200) Si on a pu récupérer une liste d'évenement corrrespondant à la recherche + la liste
     *         INTERNAL_SERVER_ERROR si on échoue à récupérer l'évènement
     *         NOT_FOUND si l'identifiant de l'évènement, de l'utilisateur ou de la série est incorrect
     *         UNAUTHORIZED si l'utilisateur n'a pas les droits suffisant pour accéder à la série de l'évènement
     */
    @GetMapping("/{id_user}/evenement/{id_serie}/{etiquette}")
    public ResponseEntity voir_evenement_par_etiquette(@PathVariable long id_user, @PathVariable("id_serie") long id_serie, @PathVariable String etiquette) {
        try {
            Iterable<Evenement> liste_event = serviceEvenement.getEvenementsEtiquetteSerie(id_serie, etiquette, id_user);
            return ResponseEntity.status(HttpStatus.OK).body(liste_event);

        } catch (ExceptionInterne err) {
            return ResponseEntity.internalServerError().body(err.getMessage());

        } catch (ExceptionNonAutoriseNonDroit err) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(err.getMessage());

        } catch (ExceptionEntiteNonTrouvee err) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err.getMessage());
        }

    }

    /**
     * Retourne une liste d'évènements d'une série en fonction d'une étiquette en mode mappage
     * @param etiquette : Critère de recherche de l'étiquette, recherche exacte (Sensible à la casse)
     * @param id_serie : identifiant de la série dans laquelle on va chercher
     * @param id_user : identifiant unique de l'utilisateur qui demande à voir les évènements
     * @return OK (200) Si on a pu récupérer une liste d'évenement corrrespondant à la recherche + la liste
     *         INTERNAL_SERVER_ERROR si on échoue à récupérer l'évènement
     *         NOT_FOUND si l'identifiant de l'évènement, de l'utilisateur ou de la série est incorrect
     *         UNAUTHORIZED si l'utilisateur n'a pas les droits suffisant pour accéder à la série de l'évènement
     */
    @GetMapping("/{id_user}/evenement/{id_serie}/mapper/{etiquette}")
    public ResponseEntity voir_evenement_par_etiquette_mapper(@PathVariable long id_user, @PathVariable("id_serie") long id_serie, @PathVariable String etiquette) {
        try {
            Iterable<Evenement> liste_event = serviceEvenement.getEvenementsEtiquetteSerie(id_serie, etiquette, id_user);
            return ResponseEntity.status(HttpStatus.OK).body(serviceEvenement.get_liste_couple_valeur_date(liste_event));

        } catch (ExceptionInterne err) {
            return ResponseEntity.internalServerError().body(err.getMessage());

        } catch (ExceptionNonAutoriseNonDroit err) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(err.getMessage());

        } catch (ExceptionEntiteNonTrouvee err) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err.getMessage());
        }

    }



    /**
     * Retourne le dernier évènement d'une série en fonction d'une étiquette
     * @param etiquette : Critère de recherche de l'étiquette, recherche exacte (Sensible à la casse)
     * @param id_serie : identifiant de la série dans laquelle on va chercher
     * @param id_user : identifiant unique de l'utilisateur qui demande à voir l'évènement
     * @return OK (200) Si on a pu récupérer l'évenement corrrespondant à la recherche + l'evenement
     *         INTERNAL_SERVER_ERROR si on échoue à récupérer l'évènement
     *         NOT_FOUND si l'identifiant de l'évènement, de l'utilisateur ou de la série est incorrect
     *         UNAUTHORIZED si l'utilisateur n'a pas les droits suffisant pour accéder à la série de l'évènement
     */
    @GetMapping("/{id_user}/evenement/{id_serie}/derniere_occurence/{etiquette}")
    public ResponseEntity voir_evenement_dernier_etiquette(@PathVariable long id_user, @PathVariable("id_serie") long id_serie, @PathVariable String etiquette) {
        try {
            Iterable<Evenement> iter = serviceEvenement.getEvenementEtiquetteSerieRecent(id_serie, etiquette, id_user);
            ArrayList<Evenement> alitst = new ArrayList<>();
            for (Evenement evt: iter) {
                alitst.add(evt);
                break;
            }
            return ResponseEntity.status(HttpStatus.OK).body(alitst.get(0));

        } catch (ExceptionInterne err) {
            return ResponseEntity.internalServerError().body(err.getMessage());

        } catch (ExceptionNonAutoriseNonDroit err) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(err.getMessage());

        } catch (ExceptionEntiteNonTrouvee err) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err.getMessage());
        }

    }


    /**
     * Retourne le temps écoulé depuis la derniere occurrence de l'évènement d'une série en fonction d'une étiquette
     * @param etiquette : Critère de recherche de l'étiquette, recherche exacte (Sensible à la casse)
     * @param id_serie : identifiant de la série dans laquelle on va chercher
     * @param id_user : identifiant unique de l'utilisateur qui demande à voir l'évènement
     * @return OK (200) Si on a pu récupérer l'évenement corrrespondant à la recherche + l'evenement
     *         INTERNAL_SERVER_ERROR si on échoue à récupérer l'évènement
     *         NOT_FOUND si l'identifiant de l'évènement, de l'utilisateur ou de la série est incorrect
     *         UNAUTHORIZED si l'utilisateur n'a pas les droits suffisant pour accéder à la série de l'évènement
     */
    @GetMapping("/{id_user}/evenement/{id_serie}/temps_derniere_occurence/{etiquette}")
    public ResponseEntity temps_evenement_derniere_occurence(@PathVariable long id_user, @PathVariable("id_serie") long id_serie, @PathVariable String etiquette) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(serviceEvenement.delai_en_jour_ecart(id_serie, etiquette, id_user));

        } catch (ExceptionInterne err) {
            return ResponseEntity.internalServerError().body(err.getMessage());

        } catch (ExceptionNonAutoriseNonDroit err) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(err.getMessage());

        } catch (ExceptionEntiteNonTrouvee err) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err.getMessage());
        }

    }


    /**
     * Retourne le nombre d'évènement pour une étiquette entre deux dates
     * @param etiquette : Critère de recherche de l'étiquette, recherche exacte (Sensible à la casse)
     * @param id_serie : identifiant de la série dans laquelle on va chercher
     * @param id_user : identifiant unique de l'utilisateur qui demande à voir l'évènement
     * @param date_debut : date de début de la recherche format : 2022-12-24
     * @param date_fin : date de fin de la recherche format : 2022-12-30
     * @return OK (200) Si on a pu récupérer l'évenement corrrespondant à la recherche + l'evenement
     *         INTERNAL_SERVER_ERROR si on échoue à récupérer l'évènement
     *         NOT_FOUND si l'identifiant de l'évènement, de l'utilisateur ou de la série est incorrect
     *         UNAUTHORIZED si l'utilisateur n'a pas les droits suffisant pour accéder à la série de l'évènement
     */
    @GetMapping("/{id_user}/evenement/{id_serie}/count/{etiquette}/{date_debut}/{date_fin}")
    public ResponseEntity<String> nombre_event_entre_deux_date(@PathVariable long id_user,
                                                           @PathVariable("id_serie") long id_serie,
                                                           @PathVariable String etiquette,
                                                           @PathVariable String date_debut,
                                                           @PathVariable String date_fin) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.FRANCE);

            Date ddate_debut = formatter.parse(date_debut);
            Date ddate_fin =  formatter.parse(date_fin);

            return ResponseEntity.status(HttpStatus.OK).body("" +
                    serviceEvenement.get_nombre_evenement_entre_deux_date(etiquette, id_serie, ddate_debut, ddate_fin, id_user)
            );

        } catch (ExceptionInterne err) {
            return ResponseEntity.internalServerError().body(err.getMessage());

        } catch (ExceptionNonAutoriseNonDroit err) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(err.getMessage());

        } catch (ExceptionEntiteNonTrouvee err) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err.getMessage());

        } catch (ParseException err) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err.getMessage());
        }

    }
}
