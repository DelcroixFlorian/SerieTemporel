package com.SerieTemporel.modele;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.lang.NonNull;

import javax.persistence.*;
import java.sql.Date;
import java.util.Objects;

@Entity
@Table
public class Evenement {

    @JsonIgnore
    public static final String NOM_ENTITE = "Evenement";

    /* Identfiant unique en base de données de l'évènement */
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id_event;

    /* Identifiant de la série de l'évènement (id de clé étrangère) */
    @Column
    @NonNull
    private long idSerie;

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

    /* Commentaire sur un évènement peut être null */
    @Column
    private String commentaire;

    /**
     * Constructeur d'un évènement
     * @param idSerie
     * @param date
     * @param valeur
     * @param etiquette
     * @param commentaire
     */
    public Evenement(long idSerie, Date date, double valeur, String etiquette, String commentaire){
        this.idSerie = idSerie;
        this.date = date;
        this.valeur = valeur;
        this.etiquette = etiquette;
        this.commentaire = commentaire;
    }

    /* Getter */
    public long getId_event() {
        return id_event;
    }

    public long getIdSerie() {
        return idSerie;
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

    public Evenement() {}

    /**
     * Verifie que les attributs d'un évènement soient corrects
     * @return true si l'étiquette valide et l'idSerie valide
     */
    public boolean verifier_evenement() {
        boolean valide = true;
        if (this.idSerie < 0) {
            valide = false;
        }
        if (this.etiquette.isEmpty() || this.etiquette.isBlank()) {
            valide = false;
        }

        return valide;
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
