package com.SerieTemporel.testControleur;

import com.SerieTemporel.Service.SerieService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.assertj.core.api.Assertions.*;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

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
    public void test_create_serie() throws Exception {
        seriecontroller.perform( MockMvcRequestBuilders
                            .post("/serie")
                            .content("{\"titre\": \"monidentifiant\",\"description\": \"monmotdepasse\", \"id_user\": \"1\"}")
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                            .andExpect(status().isCreated());
    }

    @Test 
    public void test_afficher_information_serie() throws Exception {
        seriecontroller.perform(MockMvcRequestBuilders
                            .get("/1/serie/1"))
                            .andExpect(status().isOk());
    }
}
