package com.SerieTemporel.modele;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.sql.Date;

@Entity
public class Evenement {

    @Id
    @GeneratedValue
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

    public long getId_event() {
        return id_event;
    }

    public long getId_serie() {
        return id_serie;
    }

    public Date getDate() {
        return date;
    }

    public double getValeur() {
        return valeur;
    }

    public String getEtiquette() {
        return etiquette;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public Evenement() {

    }
}
