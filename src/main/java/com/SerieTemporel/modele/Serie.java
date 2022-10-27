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
    private ArrayList<Integer> list_event;

    public Serie(String titre, String description){
        this.titre = titre;
        this.description = description;
    }

    public Serie() {

    }


    public String toJson(){
        
        return "";
    }

    public String toHTML(){
        
        return "";
    }

}
