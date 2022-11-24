package com.SerieTemporel.controller;

import com.SerieTemporel.Service.EvenementService;
import com.SerieTemporel.modele.Evenement;
import com.SerieTemporel.repository.EvenementRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/*
 * Gestion des requêtes concernant les événements
 */
@RestController
public class EvenementController {
    private final EvenementRepo repo;

    @Autowired
    EvenementService serviceEvenement;
    
    public EvenementController(EvenementRepo repo){
        this.repo = repo;
    }


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
     *
     */
    @PostMapping("/evenement/create")
    public ResponseEntity ajouter_evenement(@RequestBody Evenement new_evenement) {
        // Création de l'élément en base via le service et récupération de son identifiant
        long id_new_event = serviceEvenement.creerEvenement(new_evenement);

        // Gestion du cas d'erreur lors de la création
        if (id_new_event == -1) {
            return new ResponseEntity("Erreur, échec de la création de l'événement !" +
                                           " L'identifiant de la série n'a pas pu être identifié.",
                                      HttpStatus.BAD_REQUEST);
        }

        // On renvoi l'identifiant du nouvel événement avec 201 comme statut
        return new ResponseEntity("Id de l'évenement : " + id_new_event,
                                  HttpStatus.CREATED);
    }



    @DeleteMapping("/evenement/delete/{id}")
    public ResponseEntity supprimer_evenement(@PathVariable("id") long id) {
        Evenement evt = serviceEvenement.getEvenement(id);
        serviceEvenement.supprimerEvenement(evt);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PutMapping("/evenement/update")
    public ResponseEntity update_evenement(@RequestBody Evenement evt) {
        serviceEvenement.updateEvenement(evt);
        return ResponseEntity.ok(HttpStatus.ACCEPTED);
    }

    @GetMapping("/evenement/{id_event}")
    public ResponseEntity voir_evenement(@PathVariable("id_event") long id_event) {
        Evenement evt = serviceEvenement.getEvenement(id_event);
        return new ResponseEntity(evt, HttpStatus.OK);
    }
}
