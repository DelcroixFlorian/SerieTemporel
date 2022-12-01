package com.SerieTemporel.testControleur;

import com.SerieTemporel.Service.EvenementService;
import com.SerieTemporel.modele.Evenement;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.annotation.Testable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.SerieTemporel.controller.EvenementController;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.sql.Date;

//@Testable
@SpringBootTest
@AutoConfigureMockMvc
public class TestControleurEvenement {
    
    @Autowired
    private MockMvc eventcontroller;

    @MockBean
    private EvenementService evenementService;

    @Test
    public void contextload() throws Exception {
        assertThat(eventcontroller).isNotNull();
    }

    @Test
    public void test_create_evenement() throws Exception {
        eventcontroller.perform( MockMvcRequestBuilders
                        .post("/1/evenement/create")
                        .content("{\"id_serie\": 1,\"date\": \"2022-10-24\", \"valeur\": 150.0, \"etiquette\" :  \"Escrime\", \"commentaire\" : \"\" }")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isCreated());
    }

    @Test
    public void test_delete_evenement() throws Exception {
        eventcontroller.perform( MockMvcRequestBuilders
                        .delete("/1/evenement/delete/1"))
                       .andExpect(status().isNoContent());
    }

    @Test
    public void test_update_evenement() throws Exception {
        Date date = Date.valueOf("2022-10-24");
        Evenement event = new Evenement(1, date, 150.0,"Escrime", "");

        evenementService.creerEvenement(event, 1);
        eventcontroller.perform(MockMvcRequestBuilders
                         .put("/1/evenement/update")
                         .content("{\"id_serie\": 1,\"date\": \"2022-10-24\", \"valeur\": 150.0, \"etiquette\" :  \"Escrime\", \"commentaire\" : \"\" }")
                         .contentType(MediaType.APPLICATION_JSON)
                         .accept(MediaType.APPLICATION_JSON))
                       .andExpect(status().isCreated());
    }

    @Test
    public void test_get_evenement() throws Exception {
        eventcontroller.perform(MockMvcRequestBuilders
                        .get("/1/evenement/1"))
                        .andExpect(status().isOk());
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
