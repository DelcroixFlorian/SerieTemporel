package com.SerieTemporel.testRepo;

import com.SerieTemporel.modele.Evenement;
import com.SerieTemporel.modele.Serie;
import com.SerieTemporel.modele.Utilisateur;
import com.SerieTemporel.repository.EvenementRepo;
import org.junit.jupiter.api.Order;
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
    @Order(1)
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

    @Test
    @Order(2)
    public void test_existById() {
        boolean evt_existe = evenementRepo.existsById(1L);

        assertThat(evt_existe).isEqualTo(true);
    }

    @Test
    @Order(100)
    public void test_delete() {
        Date date = Date.valueOf("2022-10-24");
        Evenement evenement = new Evenement(1L, date, 150.0,"Escrime", "");

        evenementRepo.delete(evenement);

        assertThat(evenementRepo.existsById(1L)).isEqualTo(false);
    }

    @Test
    @Order(3)
    public void test_findById() {
        Date date = Date.valueOf("2022-10-24");
        Evenement evenement = new Evenement(1L, date, 150.0,"Escrime", "");

        Evenement evt_trouve = evenementRepo.findById(evenement.getId_event()).orElse(null);
        assertThat(evt_trouve).isNotEqualTo(null);
        assertThat(evt_trouve).hasFieldOrPropertyWithValue("idSerie", 1L);
        assertThat(evt_trouve).hasFieldOrPropertyWithValue("date", date);
        assertThat(evt_trouve).hasFieldOrPropertyWithValue("valeur", 150.0);
        assertThat(evt_trouve).hasFieldOrPropertyWithValue("etiquette", "Escrime");
        assertThat(evt_trouve).hasFieldOrPropertyWithValue("commentaire", "");
    }

    @Test
    @Order(4)
    public void test_getEvenementsByEtiquetteAndIdSerie() {
        Date date = Date.valueOf("2022-10-24");
        Evenement evenement = new Evenement(1L, date, 150.0,"escrime", "");

        Date date1 = Date.valueOf("2023-10-24");
        Evenement evenement1 = new Evenement(1L, date1, 150.0,"escrime", "");

        Iterable<Evenement> evt_trouve = evenementRepo.getEvenementsByEtiquetteAndIdSerie("escrime", 1L);

        assertThat(evt_trouve).contains(evenement);
        assertThat(evt_trouve).contains(evenement1);
    }
}
