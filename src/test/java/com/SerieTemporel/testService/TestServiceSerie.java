package com.SerieTemporel.testService;

import org.junit.jupiter.api.Test;
import org.junit.platform.commons.annotation.Testable;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.SerieTemporel.Service.SerieService;
import com.SerieTemporel.repository.SerieRepo;

@Testable
public class TestServiceSerie {

    @Mock
    SerieRepo userRepo;

    @InjectMocks
    SerieService userService;

    @Test
    public void test(){

    }
}
