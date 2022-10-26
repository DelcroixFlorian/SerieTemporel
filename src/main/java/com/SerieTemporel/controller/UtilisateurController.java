

package com.SerieTemporel.controller;

import java.util.ArrayList;

import javax.websocket.server.PathParam;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.SerieTemporel.modele.Utilisateur;
import com.SerieTemporel.repository.UtilisateurRepo;

@RestController
public class UtilisateurController {
    
    private final UtilisateurRepo repo;
    
    public UtilisateurController(UtilisateurRepo repo){
        this.repo = repo;
    }

    @GetMapping("/utilisateurs")
    public /*ArrayList<Utilisateur>*/ String getUtilisateurs(){
        return "Je passe ici";
    }
/* 
    @getMapping("/{userid}")
    public Utilisateur getUtilisateur(@PathVariable(value = "userid") long userid){

    }*/
}
