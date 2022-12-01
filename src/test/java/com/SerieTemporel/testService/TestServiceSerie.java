package com.SerieTemporel.testService;

import org.junit.jupiter.api.Test;
import org.junit.platform.commons.annotation.Testable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.SerieTemporel.Service.SerieService;
import com.SerieTemporel.repository.SerieRepo;

@Testable
@SpringBootTest
public class TestServiceSerie {

    @Mock
    SerieRepo userRepo;

    @InjectMocks
    SerieService userService;

    @Test
    public void test(){

    }
}
