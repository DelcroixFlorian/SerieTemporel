package com.SerieTemporel.modele;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.sql.Date;

@Entity
public class Evenement {

    @Id
    private long id_event;

    private long id_serie;
    private Date date;
    private double valeur;
    private String etiquette;
    private String commentaire;

    /**
     *
     * @param id_serie
     * @param date
     * @param valeur
     * @param etiquette
     * @param commentaire
     */
    public Evenement(long id_serie,Date date,double valeur,String etiquette,String commentaire){
        this.id_serie = id_serie;
        this.date = date;
        this.valeur = valeur;
        this.etiquette = etiquette;
        this.commentaire = commentaire;
    }


    public Evenement() {

    }
}
