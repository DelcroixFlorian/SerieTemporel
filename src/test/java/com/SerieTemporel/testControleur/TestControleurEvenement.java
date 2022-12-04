package com.SerieTemporel.testControleur;

import com.SerieTemporel.Service.EvenementService;
import com.SerieTemporel.modele.Evenement;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.sql.Date;

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
        when(evenementService.creerEvenement(new Evenement(), 1)).thenReturn(1L);
        eventcontroller.perform( MockMvcRequestBuilders
                        .post("/1/evenement")
                        .content("{\"id_serie\": 1,\"date\": \"2022-10-24\", \"valeur\": 150.0, \"etiquette\" :  \"Escrime\", \"commentaire\" : \"\" }")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isCreated())
                        .andExpect(content().string("Id de l'evenement : 1"));
    }

    @Test
    public void test_delete_evenement() throws Exception {
        eventcontroller.perform( MockMvcRequestBuilders
                        .delete("/1/evenement/1"))
                        .andExpect(status().isNoContent());
    }

    @Test
    public void test_update_evenement() throws Exception {
        Date date = Date.valueOf("2022-10-24");
        Evenement event = new Evenement(1, date, 150.0,"Escrime", "");

        evenementService.creerEvenement(event, 1);
        eventcontroller.perform(MockMvcRequestBuilders
                         .put("/1/evenement")
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


    @Test
    public void test_voir_evenement_par_etiquette() throws Exception {
        eventcontroller.perform(MockMvcRequestBuilders
                        .get("/1/evenement/1/escrime"))
                        .andExpect(status().isOk());
    }

    @Test
    public void test_voir_evenement_par_etiquette_mapper() throws Exception {
        eventcontroller.perform(MockMvcRequestBuilders
                        .get("/1/evenement/1/mapper/escrime"))
                        .andExpect(status().isOk());
    }


    @Test
    public void test_voir_evenement_dernier_etiquette() throws Exception {
//        eventcontroller.perform(MockMvcRequestBuilders
//                        .get("/1/evenement/1/derniere_occurence/escrime"))
//                        .andExpect(status().isOk());
    }

    @Test
    public void test_temps_evenement_derniere_occurence() throws Exception {
//        eventcontroller.perform(MockMvcRequestBuilders
//                        .get("/1/evenement/1/temps_derniere_occurence/escrime"))
//                .andExpect(status().isOk());
    }


    @Test
    public void nombre_event_entre_deux_date() throws Exception {
        eventcontroller.perform(MockMvcRequestBuilders
                        .get("/1/evenement/1/count/escrime/2022-10-20/2022-11-20"))
                        .andExpect(status().isOk());
    }
}
