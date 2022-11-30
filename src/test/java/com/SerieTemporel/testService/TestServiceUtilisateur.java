package com.SerieTemporel.testService;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.SerieTemporel.Service.UtilisateurService;
import com.SerieTemporel.repository.UtilisateurRepo;

public class TestServiceUtilisateur {

    @Mock
    UtilisateurRepo userRepo;

    @InjectMocks
    UtilisateurService userService;

    @Test
    public void test(){

    }
}
