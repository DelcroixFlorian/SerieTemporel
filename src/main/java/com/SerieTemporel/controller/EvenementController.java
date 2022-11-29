package com.SerieTemporel.controller;

import com.SerieTemporel.Service.EvenementService;
import com.SerieTemporel.exception.ExceptionNonAutoriseNonDroit;
import com.SerieTemporel.exception.ExceptionFormatObjetInvalide;
import com.SerieTemporel.exception.ExceptionInterne;
import com.SerieTemporel.modele.Evenement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/*
 * Gestion des requêtes concernant les événements
 */
@RestController
public class EvenementController {

    private final String MESSAGE_ERREUR_INTERNE = "Une erreur interne au serveur est survenue lors du traitement de l'opération, veuillez réessayer. Détail : ";

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
    @PostMapping("/{id_user}/evenement/create")
    public ResponseEntity ajouter_evenement(@PathVariable long id_user, @RequestBody Evenement new_evenement) {
        // Création de l'élément en base via le service et récupération de son identifiant
        try {
            long id_new_event = serviceEvenement.creerEvenement(new_evenement, id_user);
            // On renvoi l'identifiant du nouvel événement avec 201 comme statut
            return new ResponseEntity("Id de l'évenement : " + id_new_event,
                    HttpStatus.CREATED);

        } catch (ExceptionInterne e) {
            return ResponseEntity.internalServerError().body(MESSAGE_ERREUR_INTERNE + e.getMessage());

        } catch (ExceptionFormatObjetInvalide e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());

        } catch (ExceptionNonAutoriseNonDroit exceptionNonAutoriseNonDroit) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(exceptionNonAutoriseNonDroit.getMessage());
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
    @DeleteMapping("/{id_user}/evenement/delete/{id}")
    public ResponseEntity supprimer_evenement(@PathVariable long id_user, @PathVariable("id") long id) {
        try {
            serviceEvenement.supprimerEvenement(id, id_user);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

        } catch (ExceptionFormatObjetInvalide e) {
            return ResponseEntity.badRequest().body(e.getMessage());

        } catch (ExceptionInterne e) {
            return ResponseEntity.internalServerError().body(MESSAGE_ERREUR_INTERNE + e.getMessage());

        } catch (ExceptionNonAutoriseNonDroit exceptionNonAutoriseNonDroit) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(exceptionNonAutoriseNonDroit.getMessage());
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
    @PutMapping("/{id_user}/evenement/update")
    public ResponseEntity update_evenement(@PathVariable long id_user, @RequestBody Evenement evt) {
        try {
            Evenement evt_a_jour = serviceEvenement.updateEvenement(evt, id_user);
            return ResponseEntity.status(HttpStatus.CREATED).body(evt_a_jour);

        } catch (ExceptionFormatObjetInvalide err) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err.getMessage());

        } catch (ExceptionInterne exceptionInterne) {
            return ResponseEntity.internalServerError().body(MESSAGE_ERREUR_INTERNE + exceptionInterne.getMessage());

        } catch (ExceptionNonAutoriseNonDroit exceptionNonAutoriseNonDroit) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(exceptionNonAutoriseNonDroit.getMessage());
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

        } catch (ExceptionInterne e) {
            return ResponseEntity.internalServerError().body(MESSAGE_ERREUR_INTERNE + e.getMessage());

        } catch (ExceptionFormatObjetInvalide err) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err.getMessage());

        } catch (ExceptionNonAutoriseNonDroit exceptionNonAutoriseNonDroit) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(exceptionNonAutoriseNonDroit.getMessage());
        }

    }
}
