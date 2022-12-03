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
            return ResponseEntity.status(HttpStatus.OK).body(utilisateurService.getAllUtilisateurs());

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
    @PostMapping("/utilisateur")
    public ResponseEntity<String> ajouterUtilisateur(@RequestBody Utilisateur user) {
        try {
            Utilisateur new_user = utilisateurService.creerUtilisateur(user);
            return ResponseEntity.status(HttpStatus.CREATED).body("id de l'utilisateur : " + new_user.getId());

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
    @DeleteMapping("/utilisateur/{userid}")
    public ResponseEntity<String> deleteUtilisateur(@PathVariable("userid") long userid){
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
    @PutMapping("/utilisateur")
    public ResponseEntity updateUtilisateur(@RequestBody Utilisateur user){

        try {
            Utilisateur new_user = utilisateurService.updateUtilisateur(user);
            return ResponseEntity.status(HttpStatus.OK).body(new_user);
        } catch (ExceptionInterne e) {
            return ResponseEntity.internalServerError().body(e.getMessage());

        } catch (ExceptionFormatObjetInvalide e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }


    }


    /**
     * Vérification de l'identité d'un utilisateur
     * @param user : l'utilisateur dont on cherche à vérifier les informations
     * @return OK si bien à jour
     *         NOT_FOUND si l'utilisateur n'existe pas
     */
    @GetMapping("/utilisateur/connect")
    public ResponseEntity<String> connection(@RequestBody Utilisateur user) {
        try {
            long id_user = utilisateurService.verifier_identite(user.getIdentifiant(), user.getMdp());
            return ResponseEntity.ok().body("Votre numéro d'utilisateur :" + id_user);

        } catch (ExceptionFormatObjetInvalide e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }


    /**
     * Essaye de préparer un café dans une theière !
     */
    @GetMapping("/utilisateur/pause_cafe")
    public ResponseEntity<String> pause_cafe() {
        return ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT).build();
    }


}
