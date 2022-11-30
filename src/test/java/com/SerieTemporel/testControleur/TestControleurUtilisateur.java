package com.SerieTemporel.testControleur;

import org.junit.jupiter.api.Test;
import org.junit.platform.commons.annotation.Testable;
import static org.assertj.core.api.Assertions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.SerieTemporel.controller.UtilisateurController;

@Testable
@SpringBootTest
public class TestControleurUtilisateur {

    @Autowired
    private UtilisateurController usercontroller;

    @Test
    public void contextload() throws Exception {
        assertThat(usercontroller).isNotNull();
    }
}
