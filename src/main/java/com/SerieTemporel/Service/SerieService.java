package com.SerieTemporel.Service;

import com.SerieTemporel.exception.ExceptionFormatObjetInvalide;
import com.SerieTemporel.exception.ExceptionInterne;
import com.SerieTemporel.modele.Evenement;
import com.SerieTemporel.modele.Serie;
import com.SerieTemporel.repository.SerieRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class SerieService {

    @Autowired
    SerieRepo serieRepo;

    @Autowired
    UtilisateurService utilisateurService;

    /**
     * Créer une série
     * @param new_serie Série à insérer dans la base de données
     * @return long : l'identifiant de la série créée
     * @throws ExceptionInterne Si le serveur échoue à créer la série
     */
    public long creer_serie(Serie new_serie) throws ExceptionInterne {
        try {
            // vérifier user
            Serie serie =  serieRepo.save(new_serie);
            utilisateurService.ajouter_serie(1, serie);
            return serie.getId();
        } catch (Exception err) {
            throw new ExceptionInterne("erreur de création de la série");
        }
    }


    /**
     * Récupère les données d'une série
     * @param id : long identifiant de la série
     * @return la série si elle existe
     * @throws ExceptionInterne : Si on rencontre une erreur lors de la récupération
     * @throws ExceptionFormatObjetInvalide : Si la série n'existe pas
     */
    public Serie get_info_serie(long id) throws ExceptionInterne, ExceptionFormatObjetInvalide {
        if (!serieRepo.existsById(id)) {
            throw new ExceptionFormatObjetInvalide("Identifiant de la série incorrect.");
        }
        try {
            return serieRepo.findById(id).orElse(null);
        } catch (Exception err) {
            throw new ExceptionInterne("erreur de récupération de la série");
        }
    }

    public boolean serie_existe(Long id_serie) {
        return serieRepo.existsById(id_serie);
    }

    public void mettre_a_ajour_liste(Serie serie, Evenement event) {
        serie.ajouter_evenement_liste(event);
        serieRepo.save(serie);
    }


    public void supprimer_serie(long id) throws ExceptionFormatObjetInvalide, ExceptionInterne {
        if (!serieRepo.existsById(id)) {
            throw new ExceptionFormatObjetInvalide("Erreur, la série n'existe pas, suppression impossible.");
        }
        try {
            Serie serie = get_info_serie(id);
            serieRepo.delete(serie);
        } catch (Exception err) {
            throw new ExceptionInterne("erreur de suppression");
        }
    }

}
