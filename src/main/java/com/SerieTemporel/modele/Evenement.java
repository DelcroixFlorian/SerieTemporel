package com.SerieTemporel.modele;

import java.sql.Date;

public class Evenement {
    
    private long id_event;
    private long id_serie;
    private Date date;
    private double valeur;
    private String etiquette;
    private String commentaire;

    public Evenement(long id_serie,Date date,double valeur,String etiquette,String commentaire){
        this.id_serie = id_serie;
        this.date = date;
        this.valeur = valeur;
        this.etiquette = etiquette;
        this.commentaire = commentaire;
    }

    
}
