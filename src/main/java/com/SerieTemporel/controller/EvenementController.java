package com.SerieTemporel.controller;

import com.SerieTemporel.Service.EvenementService;
import com.SerieTemporel.exception.ExcecptionNonAutoriseNonDroit;
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
     * @return BAD_REQUEST si la série n'existe pas
     *         CREATED si l'objet a pu être créé, renvoi son identifiant
     *         INTERNAL_SERVER_ERROR si le serveur échoue à créer l'événement
     *
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
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(MESSAGE_ERREUR_INTERNE + e.getMessage());

        } catch (ExceptionFormatObjetInvalide e) {
            return ResponseEntity.badRequest().body(e.getMessage());

        } catch (ExcecptionNonAutoriseNonDroit excecptionNonAutoriseNonDroit) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(excecptionNonAutoriseNonDroit.getMessage());
        }

    }


    /**
     * Requête DELETE pour la suppression d'un événement
     * @param id : identifiant de l'évènement à supprimer
     * @return BAD_REQUEST si l'événement n'existe pas
     *         INTERNAL_SERVER_ERROR si la suppression a échoué
     *         OK si tout s'est bien passé
     */
    @DeleteMapping("/{id_user}/evenement/delete/{id}")
    public ResponseEntity supprimer_evenement(@PathVariable long id_user, @PathVariable("id") long id) {
        try {
            serviceEvenement.supprimerEvenement(id, id_user);
            return ResponseEntity.ok(HttpStatus.OK);

        } catch (ExceptionFormatObjetInvalide e) {
            return ResponseEntity.badRequest().body(e.getMessage());

        } catch (ExceptionInterne e) {
            return ResponseEntity.internalServerError().body(MESSAGE_ERREUR_INTERNE + e.getMessage());

        } catch (ExcecptionNonAutoriseNonDroit excecptionNonAutoriseNonDroit) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(excecptionNonAutoriseNonDroit.getMessage());
        }
    }


    /**
     * Requête PUT pour modifier un évènement
     * @param evt : une représentation de l'évènement à modifier
     * @return BAD_REQUEST si l'évènement n'existe pas
     *         INTERNAL_SERVER_ERROR si la mise à jour a échouée
     *         OK si tout s'est bien passé
     */
    @PutMapping("/{id_user}/evenement/update")
    public ResponseEntity update_evenement(@PathVariable long id_user, @RequestBody Evenement evt) {
        try {
            Evenement evt_a_jour = serviceEvenement.updateEvenement(evt, id_user);
            return ResponseEntity.ok(HttpStatus.OK);

        } catch (ExceptionFormatObjetInvalide err) {
            return ResponseEntity.badRequest().body(err.getMessage());

        } catch (ExceptionInterne exceptionInterne) {
            return ResponseEntity.internalServerError().body(MESSAGE_ERREUR_INTERNE + exceptionInterne.getMessage());

        } catch (ExcecptionNonAutoriseNonDroit excecptionNonAutoriseNonDroit) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(excecptionNonAutoriseNonDroit.getMessage());
        }
    }


    /**
     * Requête GET récupération d'un événement
     * @param id_event
     * @return OK (200) Si on a pu récupéré l'évènement
     *         INTERNAL_SERVER_ERROR si on échoué à récupérer l'évènement
     *         BAD_REQUEST si l'identifiant de l'évènement est incorrect
     */
    @GetMapping("/{id_user}/evenement/{id_event}")
    public ResponseEntity voir_evenement(@PathVariable long id_user, @PathVariable("id_event") long id_event) {
        try {
            Evenement evt = serviceEvenement.getEvenement(id_event, id_user);
            return new ResponseEntity(evt, HttpStatus.OK);

        } catch (ExceptionInterne e) {
            return ResponseEntity.internalServerError().body(MESSAGE_ERREUR_INTERNE + e.getMessage());

        } catch (ExceptionFormatObjetInvalide err) {
            return ResponseEntity.badRequest().body(err.getMessage());

        } catch (ExcecptionNonAutoriseNonDroit excecptionNonAutoriseNonDroit) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(excecptionNonAutoriseNonDroit.getMessage());
        }

    }
}
