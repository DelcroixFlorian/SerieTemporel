package com.SerieTemporel.controller;

import com.SerieTemporel.Service.SerieService;
import com.SerieTemporel.exception.ExceptionEntiteNonTrouvee;
import com.SerieTemporel.exception.ExceptionNonAutoriseNonDroit;
import com.SerieTemporel.exception.ExceptionArgumentIncorrect;
import com.SerieTemporel.exception.ExceptionInterne;
import com.SerieTemporel.modele.Serie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class SerieController {

    @Autowired
    SerieService serieService;

    /**
     * Requête POST pour créer une série
     * @param new_serie représentation de la série à créer
     * ex json : {"titre":"toto","description":"titit", "id_user": 1}
     * @return CREATED si la série est créée
     *         INTERNAL_SERVER_ERROR si on échoue à créer
     *         NOT_FOUND si l'utilisateur n'existe pas
     *         BAD_REQUEST Titre ou description de la série incorrect
     */
    @PostMapping("/serie")
    public ResponseEntity<String> ajouter_serie(@RequestBody Serie new_serie) {
        try {
            long id = serieService.creer_serie(new_serie);
            return ResponseEntity.status(HttpStatus.CREATED).body("Identifiant de la série : " + id);

        } catch (ExceptionInterne err) {
            return ResponseEntity.internalServerError().body(err.getMessage());

        } catch (ExceptionArgumentIncorrect err) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err.getMessage());

        } catch (ExceptionEntiteNonTrouvee err) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err.getMessage());
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
    @GetMapping("/{id_user}/serie/{id}")
    public ResponseEntity afficher_information_serie(@PathVariable long id_user, @PathVariable long id) {
        try {
            Serie serie = serieService.get_info_serie(id, id_user);
            return ResponseEntity.ok().body(serie);

        } catch (ExceptionInterne err) {
            return ResponseEntity.internalServerError().body(err.getMessage());

        } catch (ExceptionArgumentIncorrect err) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err.getMessage());

        } catch (ExceptionNonAutoriseNonDroit err) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(err.getMessage());

        } catch (ExceptionEntiteNonTrouvee err) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err.getMessage());
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
    @DeleteMapping("/{id_user}/serie/{id}")
    public ResponseEntity<String> supprimer_serie(@PathVariable long id_user, @PathVariable long id) {
        try {
            serieService.supprimer_serie(id, id_user);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

        } catch (ExceptionArgumentIncorrect err) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err.getMessage());

        } catch (ExceptionInterne err) {
            return ResponseEntity.internalServerError().body(err.getMessage());

        } catch (ExceptionNonAutoriseNonDroit err) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(err.getMessage());

        } catch (ExceptionEntiteNonTrouvee err) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err.getMessage());
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
    @GetMapping("/{id_user}/serie/{id_serie}/events")
    public ResponseEntity afficher_evenement_serie(@PathVariable long id_user,@PathVariable("id_serie") long id) {
        try {
            Serie serie = serieService.get_info_serie(id, id_user);
            return ResponseEntity.ok().body(serie.getList_event());

        } catch (ExceptionInterne err) {
            return ResponseEntity.internalServerError().body(err.getMessage());

        } catch (ExceptionArgumentIncorrect err) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err.getMessage());

        } catch (ExceptionNonAutoriseNonDroit err) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(err.getMessage());

        } catch (ExceptionEntiteNonTrouvee err) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err.getMessage());
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
    @GetMapping("/{id_user}/serie/{id_serie}/events_map")
    public ResponseEntity afficher_evenement_serie_mapper(@PathVariable long id_user,@PathVariable("id_serie") long id) {
        try {
            Serie serie = serieService.get_info_serie(id, id_user);
            return ResponseEntity.ok().body(serieService.get_liste_couple_valeur_date(serie.getList_event()));

        } catch (ExceptionInterne err) {
            return ResponseEntity.internalServerError().body(err.getMessage());

        } catch (ExceptionArgumentIncorrect err) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err.getMessage());

        } catch (ExceptionNonAutoriseNonDroit err) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(err.getMessage());

        } catch (ExceptionEntiteNonTrouvee err) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err.getMessage());
        }
    }

    /**
     * Requête PUT pour modifier une série
     * @param serie : une représentation de la serie à modifier
     * @return NOT_FOUND si la serie n'existe pas
     *         INTERNAL_SERVER_ERROR si la mise à jour a échouée
     *         CREATED si tout s'est bien passé + nouvel_serie
     *         UNAUTHORIZED si l'utilisateur n'a pas les droits de modification pour modifier à la série
     */
    @PutMapping("/{id_user}/serie")
    public ResponseEntity update_evenement(@PathVariable long id_user, @RequestBody Serie serie) {
        try {
            Serie serie_a_jour = serieService.updateSerie(serie, id_user);
            return ResponseEntity.status(HttpStatus.CREATED).body(serie_a_jour);

        } catch (ExceptionInterne err) {
            return ResponseEntity.internalServerError().body(err.getMessage());

        } catch (ExceptionNonAutoriseNonDroit err) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(err.getMessage());

        } catch (ExceptionEntiteNonTrouvee err) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err.getMessage());
        }
    }


    /**
     * Modification d'une serie avec ajout d'un utilisateur ayant des droits de consultation de la serie
     * @param id_user_init : utilisateur initiant la demande
     * @param id_user : utilisateur qui recevra les droits de partage
     * @param id_serie : identifiant de la serie concernée par le partage
     * @return NO_CONTENT si le partage réussi
     *         INTERNAL_SERVER_ERROR si on rencontre une erreur d'execution
     *         NOT_FOUND si la serie ou un utilisateur n'existe pas
     *         UNAUTHORIZED si l'utilisateur initiant la demande n'a pas les droits suffisants
     */
    @PatchMapping("/{id_user_init}/serie/{id_serie}/partage_consultation/{id_user}")
    public ResponseEntity<String> partager_serie_consultation(@PathVariable long id_user_init, @PathVariable long id_user, @PathVariable long id_serie) {
        try {
            serieService.partager_serie(id_user, id_serie, Serie.DROIT_CONSULTATION, id_user_init);
        } catch (ExceptionInterne err) {
            return ResponseEntity.internalServerError().body(err.getMessage());

        } catch (ExceptionArgumentIncorrect err) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err.getMessage());

        } catch (ExceptionNonAutoriseNonDroit err) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(err.getMessage());

        } catch (ExceptionEntiteNonTrouvee err) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err.getMessage());
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


    /**
     * Modification d'une serie avec ajout d'un utilisateur ayant des droits de modification (inclue la consultation) de la serie
     * @param id_user_init : utilisateur initiant la demande
     * @param id_user : utilisateur qui recevra les droits de partage
     * @param id_serie : identifiant de la serie concernée par le partage
     * @return NO_CONTENT si le partage réussi
     *         INTERNAL_SERVER_ERROR si on rencontre une erreur d'execution
     *         NOT_FOUND si la serie ou un utilisateur n'existe pas
     *         UNAUTHORIZED si l'utilisateur initiant la demande n'a pas les droits suffisants
     */
    @PatchMapping("/{id_user_init}/serie/{id_serie}/partage_modification/{id_user}")
    public ResponseEntity<String> partager_serie_modification(@PathVariable long id_user_init, @PathVariable long id_user, @PathVariable long id_serie) {
        try {
            serieService.partager_serie(id_user, id_serie, Serie.DROIT_MODIFICATION, id_user_init);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

        } catch (ExceptionInterne err) {
            return ResponseEntity.internalServerError().body(err.getMessage());

        } catch (ExceptionArgumentIncorrect err) {
            return ResponseEntity.badRequest().body(err.getMessage());

        } catch (ExceptionNonAutoriseNonDroit err) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(err.getMessage());

        } catch (ExceptionEntiteNonTrouvee err) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err.getMessage());
        }
    }


    /**
     * Modification d'une serie avec modification des droits de partage de la serie pour DROIT_MODIFICATION
     * @param id_user_init : utilisateur initiant la demande
     * @param id_user : utilisateur qui recevra les droits de partage
     * @param id_serie : identifiant de la serie concernée par le partage
     * @return NO_CONTENT si le partage réussi
     *         INTERNAL_SERVER_ERROR si on rencontre une erreur d'execution
     *         NOT_FOUND si la serie ou un utilisateur n'existe pas
     *         UNAUTHORIZED si l'utilisateur initiant la demande n'a pas les droits suffisants
     */
    @PatchMapping("/{id_user_init}/serie/{id_serie}/modifier_type_partage_en_modification/{id_user}")
    public ResponseEntity<String> modifier_partage_serie_modification(@PathVariable long id_user_init, @PathVariable long id_user, @PathVariable long id_serie) {
        try {
            serieService.modifier_partage_serie(id_user, id_serie, id_user_init, Serie.DROIT_MODIFICATION);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

        } catch (ExceptionInterne err) {
            return ResponseEntity.internalServerError().body(err.getMessage());

        } catch (ExceptionArgumentIncorrect err) {
            return ResponseEntity.badRequest().body(err.getMessage());

        } catch (ExceptionNonAutoriseNonDroit err) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(err.getMessage());

        } catch (ExceptionEntiteNonTrouvee err) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err.getMessage());
        }
    }


    /**
     * Modification d'une serie avec modification des droits de partage de la serie pour DROIT_CONSULTATION
     * @param id_user_init : utilisateur initiant la demande
     * @param id_user : utilisateur qui recevra les droits de partage
     * @param id_serie : identifiant de la serie concernée par le partage
     * @return NO_CONTENT si le partage réussi
     *         INTERNAL_SERVER_ERROR si on rencontre une erreur d'execution
     *         NOT_FOUND si la serie ou un utilisateur n'existe pas
     *         UNAUTHORIZED si l'utilisateur initiant la demande n'a pas les droits suffisants
     */
    @PatchMapping("/{id_user_init}/serie/{id_serie}/modifier_type_partage_en_consultation/{id_user}")
    public ResponseEntity<String> modifier_partage_serie_consultation(@PathVariable long id_user_init, @PathVariable long id_user, @PathVariable long id_serie) {
        try {
            serieService.modifier_partage_serie(id_user, id_serie, id_user_init, Serie.DROIT_CONSULTATION);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

        } catch (ExceptionInterne err) {
            return ResponseEntity.internalServerError().body(err.getMessage());

        } catch (ExceptionArgumentIncorrect e) {
            return ResponseEntity.badRequest().body(e.getMessage());

        } catch (ExceptionNonAutoriseNonDroit err) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(err.getMessage());

        } catch (ExceptionEntiteNonTrouvee err) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err.getMessage());
        }
    }


    /**
     * Retire le partage
     * @param id_user_init : utilisateur initiant la demande
     * @param id_user : utilisateur à qui on supprime le partage
     * @param id_serie : identifiant de la serie concernée
     * @return NO_CONTENT si le partage réussi
     *         INTERNAL_SERVER_ERROR si on rencontre une erreur d'execution
     *         NOT_FOUND si la serie ou un utilisateur n'existe pas
     *         UNAUTHORIZED si l'utilisateur initiant la demande n'a pas les droits suffisants
     */
    @PatchMapping("/{id_user_init}/serie/{id_serie}/supprimer_partage/{id_user}")
    public ResponseEntity<String> supprimer_partage_serie(@PathVariable long id_user_init, @PathVariable long id_user, @PathVariable long id_serie) {
        try {
            serieService.supprimer_partage_serie(id_user, id_serie, id_user_init);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

        } catch (ExceptionInterne err) {
            return ResponseEntity.internalServerError().body(err.getMessage());

        } catch (ExceptionArgumentIncorrect e) {
            return ResponseEntity.badRequest().body(e.getMessage());

        } catch (ExceptionNonAutoriseNonDroit err) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(err.getMessage());

        } catch (ExceptionEntiteNonTrouvee err) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err.getMessage());
        }
    }
}
