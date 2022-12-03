package com.SerieTemporel.testModele;

import com.SerieTemporel.modele.Evenement;

import org.junit.jupiter.api.Test;
import org.junit.platform.commons.annotation.Testable;
import static org.assertj.core.api.Assertions.*;

import java.sql.Date;

@Testable
public class TestEvenement {

    Date date = Date.valueOf("2000-12-12");
    Evenement event = new Evenement(1, date, 15.0,"ici", "la");


    @Test
    public void test_creation() {
        assertThat(event.getIdSerie()).isEqualTo(1);
    }

    @Test
    public void test_getid(){
        assertThat(event.getId_event()).isEqualTo(0);
    }

    @Test
    public void test_getSerie(){
        assertThat(event.getIdSerie()).isEqualTo(1);
    }

    @Test
    public void test_getDate(){
        assertThat(event.getDate()).isEqualTo("2000-12-12");
    }
    
    @Test
    public void test_getValeur(){
        assertThat(event.getValeur()).isEqualTo(15.0);
    }
    
    @Test
    public void test_getEtiquette(){
        assertThat(event.getEtiquette()).isEqualTo("ici");
    }
    
    @Test
    public void test_getCommentaire(){
        assertThat(event.getCommentaire()).isEqualTo("la");
    }

    @Test
    public void test_toString(){
        assertThat(event.toString()).isEqualTo("0;1;2000-12-12;15.0;ici;la");
    }
}
