package com.SerieTemporel.modele;

import java.util.ArrayList;

import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Utilisateur {
    
    @Id
    private Long id;
    
    private String identifiant;
    private String mdp;

    @Convert(converter= ConvertListeSerie.class)
    private ArrayList<Integer> list_serie;

    @Convert(converter= ConvertListeSerie.class)
    private ArrayList<Integer> shared_list_serie;

    public Utilisateur(String identifiant, String mdp){
        this.identifiant = identifiant;
        this.mdp = mdp;
        this.list_serie = null;
        this.shared_list_serie = null;
    }

    public Utilisateur() {

    }

    public Long getId() {
        return id;
    }


    public String getIdentifiant(){
        return identifiant;
    }

}
