package com.SerieTemporel.testControleur;

import org.junit.jupiter.api.Test;
import org.junit.platform.commons.annotation.Testable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.*;
import com.SerieTemporel.controller.EvenementController;

@Testable
@SpringBootTest
public class TestControleurEvenement {
    
    @Autowired
    private EvenementController eventcontroller;

    @Test
    public void contextload() throws Exception {
        assertThat(eventcontroller).isNotNull();
    }
}
