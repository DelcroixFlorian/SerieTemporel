package com.SerieTemporel.testService;

import org.aspectj.lang.annotation.Before;
import org.h2.command.ddl.CreateUser;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.annotation.Testable;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import com.SerieTemporel.Service.UtilisateurService;
import com.SerieTemporel.modele.Utilisateur;
import com.SerieTemporel.repository.UtilisateurRepo;

@RunWith(SpringRunner.class)
public class TestServiceUtilisateur {

    @TestConfiguration
    static class TestServiceUtilisateurConfig {
        
        @Bean
        public UtilisateurService userService(){
            return new UtilisateurService();
        } 
    }

    @Autowired
    private UtilisateurService userService;

    @Autowired
    private UtilisateurRepo userRepo;

    @BeforeAll
    public void setUp(){
        Utilisateur user = new Utilisateur("user", "mdp");
        Mockito.when(userRepo.findByIdentifiant(user.getIdentifiant()));
    }

    /*@Test
    public void */
}
