package com.SerieTemporel.testModele;

import com.SerieTemporel.modele.Evenement;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.annotation.Testable;
import static org.assertj.core.api.Assertions.*;

import java.sql.Date;

@Testable
public class TestEvenement {

    @Test
    public void test_creation() {
        Date date = Date.valueOf("2000-12-12");
        Evenement event = new Evenement(1, date, 15.0,"", "");
        assertThat(event.getId_serie()).isEqualTo(1);
    }
}
