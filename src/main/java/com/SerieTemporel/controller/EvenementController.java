package com.SerieTemporel.controller;

import com.SerieTemporel.modele.Evenement;
import com.SerieTemporel.modele.Serie;
import com.SerieTemporel.repository.EvenementRepo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class EvenementController {
    private final EvenementRepo repo;
    
    public EvenementController(EvenementRepo repo){
        this.repo = repo;
    }

    @PostMapping("/evenement/create")
    public ResponseEntity ajouter_evenement(@RequestBody Serie new_evenement) {
        return ResponseEntity.ok(HttpStatus.CREATED);
    }

    @DeleteMapping("/evenement/delete/{id}")
    public ResponseEntity supprimer_evenement(@RequestParam int id) {
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PatchMapping("/evenement/update/{id}")
    public ResponseEntity update_evenement(@RequestParam int id, @RequestBody Evenement evt) {
        return ResponseEntity.ok(HttpStatus.ACCEPTED);
    }
}
