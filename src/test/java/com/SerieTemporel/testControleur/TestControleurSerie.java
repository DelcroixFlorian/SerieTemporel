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


    @Test
    public void test_supprimer_serie() throws Exception {
        seriecontroller.perform(MockMvcRequestBuilders
                        .delete("/1/serie/1"))
                        .andExpect(status().isNoContent());
    }

    @Test
    public void test_afficher_evenement_serie() throws Exception {
//        seriecontroller.perform(MockMvcRequestBuilders
//                        .get("/1/serie/1/events"))
//                        .andExpect(status().isOk());
    }

    @Test
    public void test_afficher_evenement_serie_mapper() throws Exception {
//        seriecontroller.perform(MockMvcRequestBuilders
//                        .get("/1/serie/1/events_map"))
//                .andExpect(status().isOk());
    }

    @Test
    public void test_partager_serie_consultation() throws Exception {
        seriecontroller.perform(MockMvcRequestBuilders
                        .patch("/1/serie/1/partage_consultation/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void test_partager_serie_modification() throws Exception {
        seriecontroller.perform(MockMvcRequestBuilders
                        .patch("/1/serie/1/partage_modification/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void test_modifier_partage_serie_modification() throws Exception {
        seriecontroller.perform(MockMvcRequestBuilders
                        .patch("/1/serie/1/modifier_type_partage_en_modification/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void test_modifier_partage_serie_consultation() throws Exception {
        seriecontroller.perform(MockMvcRequestBuilders
                        .patch("/1/serie/1/modifier_type_partage_en_consultation/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void test_supprimer_partage() throws Exception {
        seriecontroller.perform(MockMvcRequestBuilders
                        .patch("/1/serie/1/supprimer_partage/1"))
                .andExpect(status().isNoContent());
    }

}
