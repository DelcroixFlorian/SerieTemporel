package com.SerieTemporel.modele;

import org.springframework.lang.NonNull;

import javax.persistence.*;
import java.sql.Date;
import java.util.Objects;

@Entity
@Table
public class Evenement {

    /* Identfiant unique en base de données de l'évènement */
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id_event;

    /* Identifiant de la série de l'évènement (id de clé étrangère) */
    @Column
    @NonNull
    private long id_serie;

    /* Date d'accomplissement de l'événement */
    @Column
    @NonNull
    private Date date;

    /* Valeur de l'évènement */
    @Column
    @NonNull
    private double valeur;

    /* Tag/etiquette de l'événement, permet de faire des tris et filtre */
    @Column
    @NonNull
    private String etiquette;

    /* Commentaire sur un évèenement peut être null */
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

    /* Getter */
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Evenement evenement = (Evenement) o;
        return id_event == evenement.id_event;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id_event);
    }
}
