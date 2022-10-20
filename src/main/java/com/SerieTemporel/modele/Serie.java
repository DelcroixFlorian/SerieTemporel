package com.SerieTemporel.modele;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.function.Function;

public class Serie {
    
    private long id;
    private String titre;
    private String description;


    private ArrayList<Evenement> list_event;

    public Serie(String titre, String description){
        this.titre = titre;
        this.description = description;
    }
    

    public String toJson(){
        
        return "";
    }

    public String toHTML(){
        
        return "";
    }

}
