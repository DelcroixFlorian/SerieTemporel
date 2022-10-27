

package com.SerieTemporel.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.SerieTemporel.Service.UtilisateurService;
import com.SerieTemporel.repository.UtilisateurRepo;

@RestController
public class UtilisateurController {
    
    private final UtilisateurRepo repo;

    @Autowired
    UtilisateurService utilisateurService;
    
    public UtilisateurController(UtilisateurRepo repo){
        this.repo = repo;
    }

    @GetMapping("/utilisateurs")
    public ResponseEntity getUtilisateurs(){
        return new ResponseEntity(utilisateurService.getAllUtilisateurs(), HttpStatus.OK);
    }

    @GetMapping("/utilisateur/{userid}")
    public ResponseEntity getUtilisateur(@PathVariable(value = "userid") long userid){
        return new ResponseEntity(utilisateurService.getUtilisateur(userid), HttpStatus.OK);
    }
}
