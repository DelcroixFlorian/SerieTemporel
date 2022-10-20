package com.SerieTemporel.modele;

import java.util.ArrayList;

public class Utilisateur {
    
    private long id;
    private String identifiant;
    private String mdp;
    private ArrayList<Serie> list_serie;
    private ArrayList<Serie> shared_list_serie;
    
    public Utilisateur(String identifiant, String mdp){
        this.identifiant = identifiant;
        this.mdp = mdp;
    }
}
