package com.SerieTemporel.testControleur;

import com.SerieTemporel.Service.SerieService;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.annotation.Testable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;



import static org.assertj.core.api.Assertions.*;

import com.SerieTemporel.controller.SerieController;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@Testable
@SpringBootTest
@AutoConfigureMockMvc
public class TestControleurSerie {

    @Autowired
    private MockMvc seriecontroller;

    @MockBean
    private SerieService serieService;

    @Test
    public void contextload() throws Exception {
        assertThat(seriecontroller).isNotNull();
    }

    @Test
    public void test_create_serie() {
        //seriecontroller.perform(MockMvcRequestBuilders.post(""));
    }
}
