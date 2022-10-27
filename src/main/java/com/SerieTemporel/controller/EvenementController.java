package com.SerieTemporel.controller;

import com.SerieTemporel.Service.EvenementService;
import com.SerieTemporel.modele.Evenement;
import com.SerieTemporel.modele.Serie;
import com.SerieTemporel.repository.EvenementRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class EvenementController {
    private final EvenementRepo repo;

    @Autowired
    EvenementService serviceEvenement;
    
    public EvenementController(EvenementRepo repo){
        this.repo = repo;
    }

    @PostMapping("/evenement/create")
    public ResponseEntity ajouter_evenement(@RequestBody Evenement new_evenement) {
        return new ResponseEntity("Id de l'évenement : " + serviceEvenement.creerEvenement(new_evenement),
                                  HttpStatus.CREATED);
    }

    @DeleteMapping("/evenement/delete/{id}")
    public ResponseEntity supprimer_evenement(@PathVariable("id") long id) {
        Evenement evt = serviceEvenement.getEvenement(id);
        serviceEvenement.supprimerEvenement(evt);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PatchMapping("/evenement/update")
    public ResponseEntity update_evenement(@RequestBody Evenement evt) {
        serviceEvenement.updateEvenement(evt);
        return ResponseEntity.ok(HttpStatus.ACCEPTED);
    }
}
