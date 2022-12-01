package com.SerieTemporel.testRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import com.SerieTemporel.modele.Utilisateur;
import com.SerieTemporel.repository.UtilisateurRepo;
import static org.assertj.core.api.Assertions.*;
import org.junit.runner.*;

@DataJpaTest
@RunWith(SpringRunner.class)
public class testRepoSerie {
    
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    UtilisateurRepo userRepo;

    @Test
    public void testsaveUser() throws Exception {
        /*Utilisateur user = new Utilisateur("jean", "mdp");
        entityManager.persistAndFlush(user);

        assertThat(userRepo.findById(user.getId())).isEqualTo(user);*/
    }
}
