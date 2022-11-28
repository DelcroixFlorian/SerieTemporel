package com.SerieTemporel.controller;

import com.SerieTemporel.Service.SerieService;
import com.SerieTemporel.exception.ExceptionFormatObjetInvalide;
import com.SerieTemporel.exception.ExceptionInterne;
import com.SerieTemporel.modele.Serie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class SerieController {

    private final String MESSAGE_ERREUR_INTERNE = "Une erreur interne au serveur est survenue lors du traitement de l'opération, veuillez réessayer. Détail : ";

    @Autowired
    SerieService serieService;

    /**
     * Requête POST pour créer une série
     * @param new_serie représentation de la série à créer
     * ex json : {"titre":"toto","description":"titit"}
     * @return CREATED si la série est créée
     *         INTERNAL_SERVER_ERROR si on échoue à créer
     */
    @PostMapping("/serie/create")
    public ResponseEntity ajouter_serie(@RequestBody Serie new_serie) {
        try {
            long id = serieService.creer_serie(new_serie);
            return new ResponseEntity("Identifiant de la série : " + id, HttpStatus.CREATED);

        } catch (ExceptionInterne e) {
            return ResponseEntity.internalServerError().body(MESSAGE_ERREUR_INTERNE + e.getMessage());
        }

    }


    @GetMapping("/serie/info_serie/{id}")
    public ResponseEntity afficher_information_serie(@PathVariable long id) {
        try {
            Serie serie = serieService.get_info_serie(id);
            return ResponseEntity.ok().body(serie);

        } catch (ExceptionInterne e) {
            return ResponseEntity.internalServerError().body(MESSAGE_ERREUR_INTERNE + e.getMessage());

        } catch (ExceptionFormatObjetInvalide e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @DeleteMapping("/serie/delete/{id}")
    public ResponseEntity supprimer_serie(@PathVariable long id) {
        try {
            serieService.supprimer_serie(id);
            return ResponseEntity.ok(HttpStatus.OK);

        } catch (ExceptionFormatObjetInvalide e) {
            return ResponseEntity.badRequest().body(e.getMessage());

        } catch (ExceptionInterne e) {
            return ResponseEntity.internalServerError().body(MESSAGE_ERREUR_INTERNE + e.getMessage());
        }
    }


    /**
     *
     * @param id : l'identifiant de la série à retourner
     * @return OK
     *         BAD_REQUEST
     *         INTERNAL_SERVER_ERROR
     */
    @GetMapping("/serie/afficher_tous_event/{id_serie}")
    public ResponseEntity afficher_evenement_serie(@PathVariable("id_serie") long id) {
        try {
            Serie serie = serieService.get_info_serie(id);
            return ResponseEntity.ok().body(serie.getList_event());

        } catch (ExceptionInterne e) {
            return ResponseEntity.internalServerError().body(MESSAGE_ERREUR_INTERNE + e.getMessage());

        } catch (ExceptionFormatObjetInvalide e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
