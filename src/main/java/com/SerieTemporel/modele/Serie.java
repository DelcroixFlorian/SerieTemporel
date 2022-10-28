package com.SerieTemporel.modele;

import java.util.ArrayList;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Serie {
    
    @Id
    @GeneratedValue
    private long id;
    
    private String titre;
    private String description;

    @Convert(converter = ConvertListeSerie.class)
    private ArrayList<Long> list_event;

    public Serie(String titre, String description){
        this.titre = titre;
        this.description = description;
        this.list_event = new ArrayList<>();
    }

    public Serie() {

    }


    public String toJson(){
        
        return "";
    }

    public String toHTML(){
        
        return "";
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

    public ArrayList<Long> getList_event() {
        return list_event;
    }

    public void ajouter_evenement_liste(long id) {
       list_event.add(id);
    }
}
