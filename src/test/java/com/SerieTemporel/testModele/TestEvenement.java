package com.SerieTemporel.testModele;

import com.SerieTemporel.modele.Evenement;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.annotation.Testable;
import static org.assertj.core.api.Assertions.*;

import java.sql.Date;

@Testable
public class TestEvenement {

    Date date;
    Evenement event;

    @BeforeAll
    public void init(){
        date = Date.valueOf("2000-12-12");
        event = new Evenement(1, date, 15.0,"ici", "la");
    }

    @Test
    public void test_creation() {
        assertThat(event.getId_serie()).isEqualTo(1);
    }

    @Test
    public void test_getid(){
        assertThat(event.getId_event()).isEqualTo(1);
    }

    @Test
    public void test_getSerie(){
        assertThat(event.getId_serie()).isEqualTo(1);
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
        assertThat(event.getId_serie()).isEqualTo("ici");
    }
    
    @Test
    public void test_getCommentaire(){
        assertThat(event.getCommentaire()).isEqualTo("la");
    }

    @Test
    public void test_toString(){
        assertThat(event.toString()).isEqualTo("1;1;2000-12-12;15.0;ici;la");
    }
}
