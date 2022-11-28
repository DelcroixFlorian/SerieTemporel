package com.SerieTemporel.modele;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;

@Entity
@Table
public class Serie {
    
    @Id
    @Column
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private String titre;

    @Column
    private String description;

    @Column
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name="id_serie")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<Evenement> list_event;

    public Serie(String titre, String description){
        this.titre = titre;
        this.description = description;
        this.list_event = new ArrayList<>();
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


    public List<Evenement> getList_event() {
        return list_event;
    }

    public boolean ajouter_evenement_liste(Evenement evt) {
       return list_event.add(evt);
    }
}
