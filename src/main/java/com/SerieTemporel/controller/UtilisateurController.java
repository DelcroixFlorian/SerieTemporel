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


    /**
     * Renvoi tous les utilisateurs de la base de données
     * @return OK et Une liste d'utilisateur
     *         INTERNAL_SERVER_ERROR si une erreur survient
     */
    @GetMapping("/utilisateurs")
    public ResponseEntity getUtilisateurs(){
        try {
            return new ResponseEntity(utilisateurService.getAllUtilisateurs(), HttpStatus.OK);

        } catch (ExceptionInterne e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }


    /**
     * Créé un utilisateur
     * @param user : la représentation de l'utilisateur à créer
     * @return OK + url de l'utilisateur
     *         INTERNAL_SERVER_ERROR si une erreur survient
     */
    @PostMapping("/utilisateur/create")
    public ResponseEntity ajouterUtilisateur(@RequestBody Utilisateur user) {
        try {
            return ResponseEntity.ok("/utilisateur/" + utilisateurService.creerUtilisateur(user));

        } catch (ExceptionInterne e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }


    /**
     * Récupère un utilisateur
     * @param userid : l'identifiant de l'utilisateur
     * @return OK + representation de l'utilisateur
     *         INTERNAL_SERVER_ERROR si une erreur survient
     *         NOT_FOUND si l'utilisateur n'existe pas
     */
    @GetMapping("/utilisateur/{userid}")
    public ResponseEntity getUtilisateur(@PathVariable("userid") long userid){
        try {
            return new ResponseEntity(utilisateurService.getUtilisateur(userid), HttpStatus.OK);

        } catch (ExceptionFormatObjetInvalide e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());

        } catch (ExceptionInterne e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }


    /**
     * Supprime un utilisateur
     * @param userid : identifiant de l'utilisateur
     * @return OK si supprimé
     *         INTERNAL_SERVER_ERROR si une erreur survient
     *         NOT_FOUND si l'utilisateur n'existe pas
     */
    @DeleteMapping("/utilisateur/delete/{userid}")
    public ResponseEntity deleteUtilisateur(@PathVariable("userid") long userid){
        try {
            utilisateurService.deleteUtilisateur(userid);
        } catch (ExceptionInterne e) {
            return ResponseEntity.internalServerError().body(e.getMessage());

        } catch (ExceptionFormatObjetInvalide e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }

        return ResponseEntity.ok("Utilisateur : " + userid + " supprimé.");
    }

    /**
     * Mise à jour d'un utilisateur
     * @param user : la nouvelle représentation de l'utilisateur
     * @return OK si bien à jour
     *         INTERNAL_SERVER_ERROR si une erreur survient
     *         NOT_FOUND si l'utilisateur n'existe pas
     */
    @PutMapping("/utilisateur/update")
    public ResponseEntity updateUtilisateur(@RequestBody Utilisateur user){

        try {
            utilisateurService.updateUtilisateur(user);
        } catch (ExceptionInterne e) {
            return ResponseEntity.internalServerError().body(e.getMessage());

        } catch (ExceptionFormatObjetInvalide e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }

        return ResponseEntity.ok("Utilisateur modifié");
    }


    /**
     * Vérification de l'identité d'un utilisateur
     * @param user : l'utilisateur dont on cherche à vérifier les informations
     * @return OK si bien à jour
     *         NOT_FOUND si l'utilisateur n'existe pas
     */
    @GetMapping("/utilisateur/connect")
    public ResponseEntity connection(@RequestBody Utilisateur user) {
        try {
            long id_user = utilisateurService.verifier_identite(user.getIdentifiant(), user.getMdp());
            return ResponseEntity.ok().body("Votre numéro d'utilisateur :" + id_user);

        } catch (ExceptionFormatObjetInvalide e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

}
