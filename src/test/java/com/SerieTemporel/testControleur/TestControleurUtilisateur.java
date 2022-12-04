package com.SerieTemporel.testControleur;

import com.SerieTemporel.modele.Utilisateur;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.Mockito.when;
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
        when(userService.creerUtilisateur(new Utilisateur("monidentifiant", "monmotdepasse"))).thenReturn(new Utilisateur("monidentifiant", "monmotdepasse"));
        usercontroller.perform( MockMvcRequestBuilders
                        .post("/utilisateur")
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
                        .delete("/utilisateur/1"))
                       .andExpect(status().isOk());
    }

    @Test
    public void test_updateUtilisateur() throws Exception {
        
        usercontroller.perform(MockMvcRequestBuilders
                         .put("/utilisateur")
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

    @Test
    public void test_le_cafe() throws Exception {
        usercontroller.perform(MockMvcRequestBuilders
                .get("/utilisateur/pause_cafe")
                ).andExpect(status().isIAmATeapot());
    }
}
