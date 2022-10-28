package com.SerieTemporel.controller;

import com.SerieTemporel.Service.SerieService;
import com.SerieTemporel.modele.Serie;
import com.SerieTemporel.repository.SerieRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class SerieController {
    private final SerieRepo repo;
    
    public SerieController(SerieRepo repo){
        this.repo = repo;
    }

    @Autowired
    SerieService serieService;

    @PostMapping("/serie/create")
    public ResponseEntity ajouter_serie(@RequestBody Serie new_serie) {
        long id = serieService.creer_serie(new_serie);
        return ResponseEntity.ok(HttpStatus.CREATED);
    }

    @DeleteMapping("/serie/delete/{id}")
    public ResponseEntity supprimer_serie(@RequestParam int id) {
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping("/serie/afficher_graphique/{id}")
    public ResponseEntity afficher_serie_graphique(@RequestParam int id) {
        return ResponseEntity.ok().body("graphique");
    }

    @GetMapping("/serie/afficher_tous_event/{id_serie}")
    public ResponseEntity afficher_evenement_serie(@PathVariable("id_serie") long id) {
        Serie serie = serieService.get_info_serie(id);
        return ResponseEntity.ok().body(serie);
    }
}
