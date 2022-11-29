package com.SerieTemporel.modele;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.lang.NonNull;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

@Entity
@Table
public class Utilisateur {

    /* Identifiant unique auto-incrémenté de l'utilisateur pour la base de données */
    @Id
    @GeneratedValue
    @Column
    private long id;

    /* Idenfiant de connexion d'un utilisateur (pseudo)  */
    @Column
    @NonNull
    private String identifiant;

    /* Mot de passe l'utilisateur, sauvegardé haché et salé via la librairie bcrypt */
    @Column
    @NonNull
    @JsonIgnore
    private String mdp;

    /* Liste des serie appartenant à l'utilisateur */
    @Column
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name="id_user")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<Serie> liste_serie;


    public Utilisateur(String identifiant, String mdp){
        this.identifiant = identifiant;
        this.mdp = mdp;
        this.liste_serie = new ArrayList<>();
    }

    public Utilisateur() {

    }

    public long getId() {
        return id;
    }

    public String getMdp() {
        return mdp;
    }

    public void setMdp(String mdp) {
        this.mdp = mdp;
    }

    public List<Serie> getList_serie() {
        return liste_serie;
    }


    public String getIdentifiant(){
        return identifiant;
    }

    /**
     * Ajoute une serie à l'utilisateur lors de sa creation
     * @param serie_a_ajouter : Serie a ajouter a la liste de l'utilisteur
     */
    public void ajouter_serie(Serie serie_a_ajouter) {
        liste_serie.add((int) serie_a_ajouter.getId(), serie_a_ajouter);
    }




}
