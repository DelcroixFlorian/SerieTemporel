package com.SerieTemporel.testService;

import com.SerieTemporel.Service.SerieService;
import com.SerieTemporel.exception.ExceptionArgumentIncorrect;
import com.SerieTemporel.exception.ExceptionEntiteNonTrouvee;
import com.SerieTemporel.exception.ExceptionInterne;
import com.SerieTemporel.exception.ExceptionNonAutoriseNonDroit;
import com.SerieTemporel.modele.Evenement;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.annotation.Testable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.*;

import com.SerieTemporel.Service.EvenementService;
import com.SerieTemporel.repository.EvenementRepo;

import java.sql.Date;

@Testable
@SpringBootTest
public class TestServiceEvenement {


    @Mock
    EvenementRepo evenementRepo;

    @InjectMocks
    EvenementService evenementService;

    @Test
    public void test() throws ExceptionNonAutoriseNonDroit, ExceptionInterne, ExceptionArgumentIncorrect, ExceptionEntiteNonTrouvee {
//        Date date = Date.valueOf("2022-10-24");
//        Evenement evenement = new Evenement(1, date, 150.0,"Escrime", "");
//
//        long id_user = 1;
//
//        long id_event = evenementService.creerEvenement(evenement, id_user);
//
//        assertThat(id_event).isEqualTo(1);
    }
}
