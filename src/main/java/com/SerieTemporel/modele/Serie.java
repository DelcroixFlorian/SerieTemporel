package com.SerieTemporel.modele;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import javax.persistence.*;

@Entity
@Table
public class Serie {
    
    @Id
    @Column
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private long id_user;

    @Column
    private String titre;

    @Column
    private String description;

    @Column
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name="id_serie")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<Evenement> list_event;


    @Column
    @Convert(converter = ConvertListeSerieLong.class)
    private List<Long> liste_utilisateur_partagee;


    @Column
    @Convert(converter = ConvertListeSerie.class)
    private List<String> liste_droit_serie_partagee;

    public final String DROIT_CONSULTATION = "dc";
    public final String DROIT_MODIFICATION = "dm";
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

    public long getId_user() {
        return id_user;
    }

    public List<Evenement> getList_event() {
        return list_event;
    }

    public boolean ajouter_evenement_liste(Evenement evt) {
       return list_event.add(evt);
    }

    public void ajouter_partage(Utilisateur user, String droit) {
        liste_utilisateur_partagee.add(user.getId());
        liste_droit_serie_partagee.add(droit);
    }
}
