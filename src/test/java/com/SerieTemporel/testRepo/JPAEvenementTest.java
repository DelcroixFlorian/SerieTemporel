package com.SerieTemporel.testRepo;

import com.SerieTemporel.modele.Evenement;
import com.SerieTemporel.modele.Serie;
import com.SerieTemporel.modele.Utilisateur;
import com.SerieTemporel.repository.EvenementRepo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import static org.assertj.core.api.Assertions.*;
import java.sql.Date;


@DataJpaTest
public class JPAEvenementTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    EvenementRepo evenementRepo;

    @Test
    public void test_save_evenement() {
        entityManager.persist(new Utilisateur("toto", "mdp"));
        entityManager.persist(new Serie("maSerie", "desc", 1L));

        Date date = Date.valueOf("2022-10-24");
        Evenement evenement = new Evenement(1L, date, 150.0,"Escrime", "");

        Evenement evt_save = evenementRepo.save(evenement);

        assertThat(evt_save).hasFieldOrPropertyWithValue("idSerie", 1L);
        assertThat(evt_save).hasFieldOrPropertyWithValue("date", date);
        assertThat(evt_save).hasFieldOrPropertyWithValue("valeur", 150.0);
        assertThat(evt_save).hasFieldOrPropertyWithValue("etiquette", "Escrime");
        assertThat(evt_save).hasFieldOrPropertyWithValue("commentaire", "");
    }
}
