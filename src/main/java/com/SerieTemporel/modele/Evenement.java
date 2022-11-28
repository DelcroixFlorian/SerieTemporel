package com.SerieTemporel.modele;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table
public class Evenement {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id_event;

    @Column
    private long id_serie;

    @Column
    private Date date;

    @Column
    private double valeur;

    @Column
    private String etiquette;

    @Column
    private String commentaire;

    /**
     * Constructeur d'un évènement
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

    @Override
    public String toString() {
        return  this.id_event + ";" +
                this.id_serie + ";" +
                this.date + ";" +
                this.valeur + ";" +
                this.etiquette + ";" +
                this.commentaire;
    }
}
