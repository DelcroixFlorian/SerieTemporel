package com.SerieTemporel.modele;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

@Entity
@Table
public class Utilisateur {
    
    @Id
    @GeneratedValue
    @Column
    private long id;

    @Column
    private String identifiant;

    @Column
    private String mdp;

    @Column
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name="id")
    // @OnDelete(action = OnDeleteAction.CASCADE)
    private List<Serie> list_serie;

    @Column
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name="id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<Serie> shared_list_serie;

    public Utilisateur(String identifiant, String mdp){
        this.identifiant = identifiant;
        this.mdp = mdp;
        this.list_serie = new ArrayList<>();
        this.shared_list_serie = new ArrayList<>();
    }

    public Utilisateur() {

    }

    public long getId() {
        return id;
    }


    public String getIdentifiant(){
        return identifiant;
    }

    public boolean ajouter_serie(Serie serie_a_ajouter) {
        return list_serie.add(serie_a_ajouter);
    }

}
