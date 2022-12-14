package com.SerieTemporel.modele;

import com.SerieTemporel.exception.ExceptionArgumentIncorrect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.lang.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.*;

@Entity
@Table
public class Serie {

    @JsonIgnore
    public static final String NOM_ENTITE = "Serie temporelle";

    /* Type de droit de partage */
    @JsonIgnore
    public static final String DROIT_CONSULTATION = "dc";

    @JsonIgnore
    public static final String DROIT_MODIFICATION = "dm";

    @Id
    @Column
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private long id;

    /* Identifiant de l'utilisateur propriétaire de la série (Clé étrangère) */
    @Column
    @NonNull
    private long id_user;

    /* Titre de la série */
    @Column
    @NonNull
    private String titre;

    /* Description de la série */
    @Column
    @NonNull
    private String description;

    /* Liste des évènements appartenant à la série */
    @Column
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name="idSerie")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<Evenement> list_event;


    /* Liste des identifiants des utilisateurs avec qui la série est partagée */
    @Column
    @Convert(converter = ConvertListeSerieLong.class)
    private List<Long> liste_utilisateur_partagee;


    /* Liste des droits correspondant aux partages utilisateurs */
    @Column
    @Convert(converter = ConvertListeSerie.class)
    private List<String> liste_droit_serie_partagee;

    public Serie(String titre, String description, Long id_user){
        this.titre = titre;
        this.description = description;
        this.id_user = id_user;
        this.list_event = new ArrayList<>();
        this.liste_utilisateur_partagee = new ArrayList<>();
        this.liste_droit_serie_partagee = new ArrayList<>();
    }

    public Serie() {

    }

    public long getId() {
        return id;
    }

    public String getTitre() {
        return titre;
    }

    public String getDescription() {
        return description;
    }

    public List<Long> getListe_utilisateur_partagee() {
        return liste_utilisateur_partagee;
    }

    public List<String> getListe_droit_serie_partagee() {
        return liste_droit_serie_partagee;
    }

    public long getId_user() {
        return id_user;
    }

    public List<Evenement> getList_event() {
        return list_event;
    }

    /* Ajout d'un événement à la liste de la série */
    public void ajouter_evenement_liste(Evenement evt) {
       list_event.add(evt);
    }

    /**
     * Partage de la série avec un nouvel utilisateur
     * Enregsitrement de l'identifiant de l'utilisateur et des droits associés
     * @param user : l'utilisateur à qui on va octroyer des droits
     * @param droit : le type de droit octroyé
     */
    public void ajouter_partage(Utilisateur user, String droit) {
        liste_utilisateur_partagee.add(user.getId());
        liste_droit_serie_partagee.add(droit);
    }

    /**
     * Modification du partage de la série
     * Modification des droits associés
     * @param user : l'utilisateur à qui est partagé la série
     * @param nouveau_droit : le type de droit octroyé
     */
    public void modifier_partage(Utilisateur user, String nouveau_droit) throws ExceptionArgumentIncorrect {
        int index = liste_utilisateur_partagee.indexOf(user.getId());
        if (index != -1) {
            liste_droit_serie_partagee.set(index, nouveau_droit);
        } else {
            throw new ExceptionArgumentIncorrect("Cette série n'est pas partagée");
        }
    }


    /**
     * Suppresion du partage de la série
     * @param user : l'utilisateur à qui est partagé la série
     */
    public void supprimer_partage(Utilisateur user) throws ExceptionArgumentIncorrect {
        int index = liste_utilisateur_partagee.indexOf(user.getId());
        if (index != -1) {
            liste_droit_serie_partagee.remove(index);
            liste_utilisateur_partagee.remove(index);
        } else {
            throw new ExceptionArgumentIncorrect("Cette série n'est pas partagée");
        }
    }


    /**
     * Vérifie que le titre et la description de l'entité soient valide
     * @return true si valide
     */
    public boolean verifier_argument() {
        return !this.titre.isBlank() && !this.titre.isEmpty() && !this.description.isBlank() && !this.description.isEmpty();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Serie)) return false;
        Serie serie = (Serie) o;
        return id == serie.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
