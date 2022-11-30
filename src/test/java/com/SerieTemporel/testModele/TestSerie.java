package com.SerieTemporel.testModele;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.annotation.Testable;


import static org.assertj.core.api.Assertions.*;

import java.sql.Date;
import java.util.Arrays;

import com.SerieTemporel.modele.Evenement;
import com.SerieTemporel.modele.Serie;
import com.SerieTemporel.modele.Utilisateur;

@Testable
public class TestSerie {

    Serie serie;
    Utilisateur user;
    Evenement event;
    Date date;

    @BeforeAll
    public void init(){
        user = new Utilisateur("monid","monmotdepasse");
        date = Date.valueOf("2000-12-12");
        serie = new Serie("monTitre","maDescription",user.getId());
        event = new Evenement(serie.getId(),date,15.0,"etiquete","commentaire");
    }

    @Test
    @Order(1)
    public void test_ajoutPartage(){
        serie.ajouter_partage(user, "dc");
        assertThat(serie.getListe_droit_serie_partagee()).isEqualTo(Arrays.asList("dc"));
        assertThat(serie.getListe_utilisateur_partagee()).isEqualTo(Arrays.asList(user.getId()));
    }

    @Test
    @Order(2)
    public void test_listevent(){
        serie.ajouter_evenement_liste(event);
        assertThat(serie.getList_event()).isEqualTo(Arrays.asList(event));
    }

    @Test
    public void test_creation(){
        assertThat(serie.getId()).isEqualTo(1);
    }

    @Test
    public void test_getid(){
        assertThat(serie.getId()).isEqualTo(1);
    }

    @Test
    public void test_gettitre(){
        assertThat(serie.getTitre()).isEqualTo("monTitre");
    }

    @Test
    public void test_getDescription(){
        assertThat(serie.getDescription()).isEqualTo("maDescription");
    }
    
    @Test
    public void test_getListUser(){
        assertThat(serie.getListe_utilisateur_partagee()).isEqualTo(Arrays.asList(user.getId()));
    }
    
    @Test
    public void test_getListDroit(){
        assertThat(serie.getListe_droit_serie_partagee()).isEqualTo(Arrays.asList("dc"));
    }
    
    @Test
    public void test_getIdUser(){
        assertThat(serie.getId_user()).isEqualTo(1);
    }

    @Test
    public void test_getListEvent(){
        assertThat(serie.getList_event()).isEqualTo(Arrays.asList(event));
    }
}
