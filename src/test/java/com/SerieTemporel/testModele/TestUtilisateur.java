package com.SerieTemporel.testModele;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.annotation.Testable;
import org.springframework.core.annotation.Order;

import com.SerieTemporel.modele.Serie;
import com.SerieTemporel.modele.Utilisateur;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;

@Testable
public class TestUtilisateur {
    
    
    Utilisateur user;
    Serie serie;
    

    @BeforeAll
    public void init(){
        user = new Utilisateur("monid","monmotdepasse");
        serie = new Serie("montitre", "madesccription", user.getId());
    }

    @Test
    public void test_creation(){
        assertThat(user.getId()).isEqualTo(1);
    }

    @Test
    public void test_getid(){
        assertThat(user.getId()).isEqualTo(1);
    }

    @Test
    public void test_getmdp(){
        assertThat(user.getMdp()).isEqualTo("monmotdepasse");
    }

    @Test
    public void test_setmdp(){
        user.setMdp("monnouveaumotdepasse");
        assertThat(user.getMdp()).isEqualTo("monnouveaumotdepasse");
    }
    
    @Test
    public void test_getListSerie(){
        assertThat(user.getList_serie()).isEqualTo(Arrays.asList(serie));
    }
    
    @Test
    public void test_getIdentifiant(){
        assertThat(user.getIdentifiant()).isEqualTo("monid");
    }

    @Test
    @Order(1)
    public void test_ajouterSerie(){
        user.ajouter_serie(serie);
        assertThat(user.getList_serie()).isEqualTo(Arrays.asList(serie));
    }
}
