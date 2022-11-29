package com.SerieTemporel.controller;

import com.SerieTemporel.Service.SerieService;
import com.SerieTemporel.exception.ExceptionNonAutoriseNonDroit;
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
     * ex json : {"titre":"toto","description":"titit", "id_user": 1}
     * @return CREATED si la série est créée
     *         INTERNAL_SERVER_ERROR si on échoue à créer
     *         NOT_FOUND si l'utilisateur n'existe pas
     */
    @PostMapping("/serie/create")
    public ResponseEntity ajouter_serie(@RequestBody Serie new_serie) {
        try {
            long id = serieService.creer_serie(new_serie);
            return new ResponseEntity("Identifiant de la série : " + id, HttpStatus.CREATED);

        } catch (ExceptionInterne e) {
            return ResponseEntity.internalServerError().body(MESSAGE_ERREUR_INTERNE + e.getMessage());

        } catch (ExceptionFormatObjetInvalide exceptionFormatObjetInvalide) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exceptionFormatObjetInvalide.getMessage());
        }

    }


    /**
     * Affiche toutes les informations d'une serie
     * @param id_user : identifiant de l'utilisateur initiant la demande
     * @param id : identifiant de la serie dont on veut les informations
     * @return OK + information de la serie
     *         INTERNAL_SERVER_ERROR si on rencontre une erreur d'execution
     *         NOT_FOUND si la serie n'existe pas
     *         UNAUTHORIZED si l'utilisateur n'a pas les droits suffisants
     */
    @GetMapping("/{id_user}/serie/info_serie/{id}")
    public ResponseEntity afficher_information_serie(@PathVariable long id_user, @PathVariable long id) {
        try {
            Serie serie = serieService.get_info_serie(id, id_user);
            return ResponseEntity.ok().body(serie);

        } catch (ExceptionInterne e) {
            return ResponseEntity.internalServerError().body(MESSAGE_ERREUR_INTERNE + e.getMessage());

        } catch (ExceptionFormatObjetInvalide e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());

        } catch (ExceptionNonAutoriseNonDroit exceptionNonAutoriseNonDroit) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(exceptionNonAutoriseNonDroit.getMessage());
        }
    }


    /**
     * Supprime une série
     * @param id_user : identifiant de l'utilisateur initiant la demande
     * @param id : identifiant de la serie qu'on veut supprimer
     * @return NO_CONTENT si tous c bien passé
     *         INTERNAL_SERVER_ERROR si on rencontre une erreur d'execution
     *         NOT_FOUND si la serie n'existe pas
     *         UNAUTHORIZED si l'utilisateur n'a pas les droits suffisants
     */
    @DeleteMapping("/{id_user}/serie/delete/{id}")
    public ResponseEntity supprimer_serie(@PathVariable long id_user, @PathVariable long id) {
        try {
            serieService.supprimer_serie(id, id_user);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

        } catch (ExceptionFormatObjetInvalide e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());

        } catch (ExceptionInterne e) {
            return ResponseEntity.internalServerError().body(MESSAGE_ERREUR_INTERNE + e.getMessage());

        } catch (ExceptionNonAutoriseNonDroit exceptionNonAutoriseNonDroit) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(exceptionNonAutoriseNonDroit.getMessage());
        }
    }


    /**
     * Renvoi tous les événements d'une serie
     * @param id_user : identifiant de l'utilisateur initiant la demande
     * @param id : identifiant de la serie dont on veut les événement
     * @return OK avec la liste des évenements
     *         INTERNAL_SERVER_ERROR si on rencontre une erreur d'execution
     *         NOT_FOUND si la serie n'existe pas
     *         UNAUTHORIZED si l'utilisateur n'a pas les droits suffisants
     */
    @GetMapping("/{id_user}/serie/afficher_tous_event/{id_serie}")
    public ResponseEntity afficher_evenement_serie(@PathVariable long id_user,@PathVariable("id_serie") long id) {
        try {
            Serie serie = serieService.get_info_serie(id, id_user);
            return ResponseEntity.ok().body(serie.getList_event());

        } catch (ExceptionInterne e) {
            return ResponseEntity.internalServerError().body(MESSAGE_ERREUR_INTERNE + e.getMessage());

        } catch (ExceptionFormatObjetInvalide e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());

        } catch (ExceptionNonAutoriseNonDroit exceptionNonAutoriseNonDroit) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(exceptionNonAutoriseNonDroit.getMessage());
        }
    }


    /**
     * Modification d'une serie avec ajout d'un utilisateur ayant des droits de consultation de la serie
     * @param id_user_init : utilisateur initiant la demande
     * @param id_user : utilisateur qui recevra les droits de partage
     * @param id_serie : identifiant de la serie concernée par la serie
     * @return NO_CONTENT si le partage réussi
     *         INTERNAL_SERVER_ERROR si on rencontre une erreur d'execution
     *         NOT_FOUND si la serie ou un utilisateur n'existe pas
     *         UNAUTHORIZED si l'utilisateur initiant la demande n'a pas les droits suffisants
     */
    @PatchMapping("/{id_user_init}/serie/{id_serie}/partage_consultation/{id_user}")
    public ResponseEntity partager_serie_consultation(@PathVariable long id_user_init, @PathVariable long id_user, @PathVariable long id_serie) {
        try {
            serieService.partager_serie(id_user, id_serie, 1, id_user_init);
        } catch (ExceptionInterne e) {
            return ResponseEntity.internalServerError().body(MESSAGE_ERREUR_INTERNE + e.getMessage());

        } catch (ExceptionFormatObjetInvalide e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());

        } catch (ExceptionNonAutoriseNonDroit exceptionNonAutoriseNonDroit) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(exceptionNonAutoriseNonDroit.getMessage());
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


    /**
     * Modification d'une serie avec ajout d'un utilisateur ayant des droits de modification (inclue la consultation) de la serie
     * @param id_user_init : utilisateur initiant la demande
     * @param id_user : utilisateur qui recevra les droits de partage
     * @param id_serie : identifiant de la serie concernée par la serie
     * @return NO_CONTENT si le partage réussi
     *         INTERNAL_SERVER_ERROR si on rencontre une erreur d'execution
     *         NOT_FOUND si la serie ou un utilisateur n'existe pas
     *         UNAUTHORIZED si l'utilisateur initiant la demande n'a pas les droits suffisants
     */
    @PatchMapping("/{id_user_init}/serie/{id_serie}/partage_modification/{id_user}")
    public ResponseEntity partager_serie_modification(@PathVariable long id_user_init, @PathVariable long id_user, @PathVariable long id_serie) {
        try {
            serieService.partager_serie(id_user, id_serie, 2, id_user_init);
        } catch (ExceptionInterne e) {
            return ResponseEntity.internalServerError().body(MESSAGE_ERREUR_INTERNE + e.getMessage());

        } catch (ExceptionFormatObjetInvalide e) {
            return ResponseEntity.badRequest().body(e.getMessage());

        } catch (ExceptionNonAutoriseNonDroit exceptionNonAutoriseNonDroit) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(exceptionNonAutoriseNonDroit.getMessage());
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
