package com.SerieTemporel.testService;

import org.junit.jupiter.api.Test;
import org.junit.platform.commons.annotation.Testable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.SerieTemporel.Service.EvenementService;
import com.SerieTemporel.repository.EvenementRepo;

@Testable
@SpringBootTest
public class TestServiceEvenement {

    @Mock
    EvenementRepo userRepo;

    @InjectMocks
    EvenementService userService;

    @Test
    public void test(){

    }
}
