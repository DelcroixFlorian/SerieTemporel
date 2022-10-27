

package com.SerieTemporel.controller;

import com.SerieTemporel.modele.Utilisateur;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/utilisateur/create")
    public ResponseEntity ajouterUtilisateur(@RequestBody Utilisateur user) {
        return ResponseEntity.ok("/utilisateur/" + utilisateurService.creerUtilisateur(user));
    }

    @GetMapping("/utilisateur/{userid}")
    public ResponseEntity getUtilisateur(@PathVariable("userid") long userid){
        return new ResponseEntity(utilisateurService.getUtilisateur(userid), HttpStatus.OK);
    }

    @DeleteMapping("/utilisateur/delete/{userid}")
    public ResponseEntity deleteUtilisateur(@PathVariable("userid") long userid){
        try {
            utilisateurService.deleteUtilisateur(userid);
        } catch (Exception exception) {
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity("Employee deleted with id: " + userid, HttpStatus.OK);
    }
}
