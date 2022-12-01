package com.SerieTemporel.testControleur;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.SerieTemporel.Service.UtilisateurService;

import org.springframework.http.MediaType;


@SpringBootTest
@AutoConfigureMockMvc
public class TestControleurUtilisateur {

    @MockBean
    private UtilisateurService userService;

    @Autowired
    private MockMvc usercontroller;

    @Test
    public void contextload() throws Exception {
        assertThat(usercontroller).isNotNull();
    }

    @Test
    public void test_ajouterUtilisateur() throws Exception {
        usercontroller.perform( MockMvcRequestBuilders
                        .post("/utilisateur/create")
                        .content("{\"identifiant\": \"monidentifiant\",\"mdp\": \"monmotdepasse\"}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isCreated());
    }

    @Test
    public void test_getAllUser() throws Exception {
        usercontroller.perform(MockMvcRequestBuilders
                            .get("/utilisateurs"))
                            .andExpect(status().isOk());
    }

    @Test
    public void test_getUser() throws Exception {
        usercontroller.perform(MockMvcRequestBuilders
                            .get("/utilisateur/1"))
                            .andExpect(status().isOk());
    }

    @Test
    public void test_deleteUtilisateur() throws Exception {
        usercontroller.perform( MockMvcRequestBuilders
                        .delete("/utilisateur/delete/1"))
                       .andExpect(status().isOk());
    }

    @Test
    public void test_updateUtilisateur() throws Exception {
        
        usercontroller.perform(MockMvcRequestBuilders
                         .put("/utilisateur/update")
                         .content("{\"identifiant\": \"monidentifiant\",\"mdp\": \"monmotdepasse\"}")
                         .contentType(MediaType.APPLICATION_JSON)
                         .accept(MediaType.APPLICATION_JSON))
                       .andExpect(status().isOk());
    }

    @Test
    public void test_connection() throws Exception {
        usercontroller.perform(MockMvcRequestBuilders
                            .get("/utilisateur/connect")
                            .content("{\"identifiant\": \"monidentifiant\",\"mdp\": \"monmotdepasse\"}")
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                            .andExpect(status().isOk());
    }
}
