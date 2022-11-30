package com.SerieTemporel.testRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import com.SerieTemporel.modele.Utilisateur;
import com.SerieTemporel.repository.UtilisateurRepo;

import static org.assertj.core.api.Assertions.*;


@DataJpaTest
public class testRepoSerie {
    
    @Autowired
    TestEntityManager entityManager;

    @Autowired
    UtilisateurRepo userRepo;

    @Test
    public void saveUser(){
        Utilisateur user = new Utilisateur();
        user = entityManager.persistAndFlush(user);

        assertThat(userRepo.findById(user.getId()).get()).isEqualTo(user);
    }
}
