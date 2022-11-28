package com.SerieTemporel.controller;

import com.SerieTemporel.exception.ExceptionFormatObjetInvalide;
import com.SerieTemporel.exception.ExceptionInterne;
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
        try {
            return new ResponseEntity(utilisateurService.getAllUtilisateurs(), HttpStatus.OK);
        } catch (ExceptionInterne e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PostMapping("/utilisateur/create")
    public ResponseEntity ajouterUtilisateur(@RequestBody Utilisateur user) {
        try {
            return ResponseEntity.ok("/utilisateur/" + utilisateurService.creerUtilisateur(user));
        } catch (ExceptionInterne e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping("/utilisateur/{userid}")
    public ResponseEntity getUtilisateur(@PathVariable("userid") long userid){
        try {
            return new ResponseEntity(utilisateurService.getUtilisateur(userid), HttpStatus.OK);
        } catch (ExceptionFormatObjetInvalide e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (ExceptionInterne e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @DeleteMapping("/utilisateur/delete/{userid}")
    public ResponseEntity deleteUtilisateur(@PathVariable("userid") long userid){
        try {
            utilisateurService.deleteUtilisateur(userid);
        } catch (Exception exception) {
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity("Utilisateur : " + userid + " supprimé", HttpStatus.OK);
    }

    @PutMapping("/utilisateur/update")
    public ResponseEntity updateUtilisateur(@RequestBody Utilisateur user){
        try {
            utilisateurService.updateUtilisateur(user);
        } catch(Exception exception) {
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity("Utilisateur modifié", HttpStatus.OK);
    }


    @GetMapping("/utilisateur/connect/{identifiant}")
    public ResponseEntity connection(@PathVariable String identifiant) {

        return ResponseEntity.ok().body(utilisateurService.verifier_identite(identifiant, ""));
    }
}
