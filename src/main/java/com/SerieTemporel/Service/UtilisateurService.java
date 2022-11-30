package com.SerieTemporel.Service;

import java.util.ArrayList;
import java.util.List;

import com.SerieTemporel.exception.ExceptionFormatObjetInvalide;
import com.SerieTemporel.exception.ExceptionInterne;
import com.SerieTemporel.modele.Serie;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.SerieTemporel.modele.Utilisateur;
import com.SerieTemporel.repository.UtilisateurRepo;

@Service
public class UtilisateurService {
    
    @Autowired
    UtilisateurRepo utilisateurRepository;

    @Autowired
    SerieService serieService;


    /**
     * Récupère la liste de tous les utilisateurs
     * @return la liste de tous les utilisateurs
     * @throws ExceptionInterne : si la récupération échoue
     */
    public List<Utilisateur> getAllUtilisateurs() throws ExceptionInterne {
        try {
            return new ArrayList<Utilisateur>(utilisateurRepository.findAll());
        } catch (Exception err) {
            throw  new ExceptionInterne("erreur de récupération des utilisateurs");
        }
    }

<<<<<<< Updated upstream

    /**
     * Récupère un utilisateur
     * @param userid : l'identifiant de l'utilisateur à chercher
     * @return l'utilisateur si il existe
     * @throws ExceptionFormatObjetInvalide : si l'utilisateur n'existe pas
     * @throws ExceptionInterne : si on arrive pas à récupérer l'utilisateur
     */
=======
    @Cacheable("utilisateur")
>>>>>>> Stashed changes
    public Utilisateur getUtilisateur(Long userid) throws ExceptionFormatObjetInvalide, ExceptionInterne {
        if (!utilisateurRepository.existsById(userid)) {
            throw new ExceptionFormatObjetInvalide("Utilisateur inconnu, mise à jour impossible.");
        }

        try {
            return utilisateurRepository.findById(userid).orElse(null);
        } catch (Exception err) {
            throw  new ExceptionInterne("erreur de récupération");
        }

    }


    /**
     * Création d'un utilisateur
     * Hache et sale le mot de passe avant enregistrement
     * @param user : l'utilisateur à ajouter en base de données
     * @return Le nouvel utilisateur
     * @throws ExceptionInterne : si on échoue à créer de l'utilisateur
     */
    public Long creerUtilisateur(Utilisateur user) throws ExceptionInterne {
        try {
            String mot_de_passe_hache = BCrypt.hashpw(user.getMdp(), BCrypt.gensalt());
            user.setMdp(mot_de_passe_hache);
            Utilisateur new_user = utilisateurRepository.save(user);
            return new_user.getId();
        } catch (Exception err) {
            throw  new ExceptionInterne("erreur de creation");
        }
    }


    /**
     * Met à jour un utilisateur
     * @param user : L'utilisateur à mettre à jour
     * @throws ExceptionInterne : si on échoue à mettre à jour
     * @throws ExceptionFormatObjetInvalide : si l'utilisateur n'existe pas
     */
    public void updateUtilisateur(Utilisateur user) throws ExceptionInterne, ExceptionFormatObjetInvalide {
        if (!utilisateurRepository.existsById(user.getId())) {
            throw new ExceptionFormatObjetInvalide("Utilisateur inconnu, mise à jour impossible.");
        }
        try {
            utilisateurRepository.save(user);
        } catch (Exception err) {
            throw new ExceptionInterne("erreur de mise a jour");
        }

    }


    /**
     * Suppression d'un utilisateur
     * @param userid : identifiant de l'utilisateur à supprimer
     * @throws ExceptionInterne : si on échoué à supprimer
     * @throws ExceptionFormatObjetInvalide : si l'utilisateur n'existe pas
     */
    public void deleteUtilisateur(Long userid) throws ExceptionInterne, ExceptionFormatObjetInvalide {
        if (!utilisateurRepository.existsById(userid)) {
            throw new ExceptionFormatObjetInvalide("Utilisateur inconnu, suppression impossible.");
        }
        try {
            utilisateurRepository.deleteById(userid);
        } catch (Exception err) {
            throw new ExceptionInterne("erreur de suppression");
        }

    }


    /**
     * Ajoute une série à un utilisateur
     * @param serie : la serie à ajouter
     * @throws ExceptionInterne : si on échoue à mettre à jour
     */
    public void ajouter_serie(Serie serie) throws ExceptionInterne {
        try {
            utilisateurRepository.findById(serie.getId_user()).ifPresent(user -> user.ajouter_serie(serie));
        } catch (Exception err) {
            throw new ExceptionInterne("erreur d'ajout de la serie à l'utilisateur");
        }
    }


    /**
     * Vérifie la validite du coupe identifiant(pseudo) / mot de passe d'un utilisateur
     * Utilisation de la bibliothèque bcrypt
     * @param identifiant : String : identifiant de connexion (pseudo) de l'utilisateur
     * @param mot_de_passe : String : mot de passe de l'utilisateur
     * @return l'identifiant unique de l'utilisateur
     * @throws ExceptionFormatObjetInvalide : si l'identifiant est inconnu ou le mot de passe incorrect
     */
    public long verifier_identite(String identifiant, String mot_de_passe) throws ExceptionFormatObjetInvalide {
        Iterable<Utilisateur> users = utilisateurRepository.findByIdentifiant(identifiant);
        for (Utilisateur user: users) {
            if (BCrypt.checkpw(mot_de_passe, user.getMdp())) {
                return user.getId();
            } else {
                throw new ExceptionFormatObjetInvalide("Le mot de passe ne correspond pas à l'identifiant.");
            }
        }
        throw new ExceptionFormatObjetInvalide("Aucun utilisateur ne correspond à cet identifiant.");
    }


    /**
     * Vérifie que l'utilisateur qui doit créer une série existe bien
     * @param serie La serie qui doit être créée
     * @throws ExceptionFormatObjetInvalide : si l'utilisateur n'existe pas
     */
    public void verifier_existe(Serie serie) throws ExceptionFormatObjetInvalide {
        if (!utilisateurRepository.existsById(serie.getId_user())) {
            throw new ExceptionFormatObjetInvalide("Utilisateur inconnu, création de la série impossible.");
        }
    }

}
