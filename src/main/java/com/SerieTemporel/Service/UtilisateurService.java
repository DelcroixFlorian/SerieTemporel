package com.SerieTemporel.Service;

import java.util.ArrayList;
import java.util.List;

import com.SerieTemporel.exception.ExceptionArgumentIncorrect;
import com.SerieTemporel.exception.ExceptionEntiteNonTrouvee;
import com.SerieTemporel.exception.ExceptionInterne;
import com.SerieTemporel.modele.Serie;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
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
            return new ArrayList<>(utilisateurRepository.findAll());
        } catch (Exception err) {
            throw  new ExceptionInterne("erreur de récupération des utilisateurs");
        }
    }


    /**
     * Récupère un utilisateur
     * @param id_user : l'identifiant de l'utilisateur à chercher
     * @return l'utilisateur si il existe
     * @throws ExceptionEntiteNonTrouvee : si l'utilisateur n'existe pas
     * @throws ExceptionInterne : si on arrive pas à récupérer l'utilisateur
     */
    @Cacheable("utilisateur")
    public Utilisateur getUtilisateur(Long id_user) throws ExceptionEntiteNonTrouvee, ExceptionInterne {
        if (!utilisateurRepository.existsById(id_user)) {
            throw new ExceptionEntiteNonTrouvee(Utilisateur.NOM_ENTITE, id_user, "Utilisateur inconnu, mise à jour impossible.");
        }

        try {
            return utilisateurRepository.findById(id_user).orElse(null);
        } catch (Exception err) {
            throw new ExceptionInterne("erreur de récupération");
        }

    }


    /**
     * Création d'un utilisateur
     * Hache et sale le mot de passe avant enregistrement
     * @param user : l'utilisateur à ajouter en base de données
     * @return Le nouvel utilisateur
     * @throws ExceptionInterne : si on échoue à créer de l'utilisateur
     * @throws ExceptionArgumentIncorrect : si l'idenfifiant est déjà pris
     */
    public Utilisateur creerUtilisateur(Utilisateur user) throws ExceptionInterne, ExceptionArgumentIncorrect {
        // Vérification nom d'utilisateur disponible
        if (utilisateurRepository.findByIdentifiant(user.getIdentifiant()) != null) {
            throw new ExceptionArgumentIncorrect("L'idenfiant est déjà pris veuillez en choisir un autre.");
        }
        // Enregistrement
        try {
            String mot_de_passe_hache = BCrypt.hashpw(user.getMdp(), BCrypt.gensalt());
            user.setMdp(mot_de_passe_hache);
            return utilisateurRepository.save(user);

        } catch (Exception err) {
            throw  new ExceptionInterne("erreur de creation");
        }
    }


    /**
     * Met à jour un utilisateur
     * @param user : L'utilisateur à mettre à jour
     * @throws ExceptionInterne : si on échoue à mettre à jour
     * @throws ExceptionEntiteNonTrouvee : si l'utilisateur n'existe pas
     * @throws ExceptionArgumentIncorrect : si le mot de passe ou l'identifiant sont vides
     */
    @Cacheable(value="utilisateur", key="#user.id")
    @CacheEvict(value="utilisateur", key="#user.id")
    public Utilisateur updateUtilisateur(Utilisateur user) throws ExceptionInterne, ExceptionArgumentIncorrect, ExceptionEntiteNonTrouvee {
        if (!utilisateurRepository.existsById(user.getId())) {
            throw new ExceptionEntiteNonTrouvee(Utilisateur.NOM_ENTITE, user.getId(), "Utilisateur inconnu, mise à jour impossible.");
        }

        if (!user.verifier_argument()) {
            throw new ExceptionArgumentIncorrect("L'identifiant et/ou le mot de passe ne doivent pas être vide.");
        }

        try {
            return utilisateurRepository.save(user);

        } catch (Exception err) {
            throw new ExceptionInterne("erreur de mise a jour");
        }

    }


    /**
     * Suppression d'un utilisateur
     * @param id_user : identifiant de l'utilisateur à supprimer
     * @throws ExceptionInterne : si on échoue à supprimer
     * @throws ExceptionEntiteNonTrouvee : si l'utilisateur n'existe pas
     */
    @Caching(evict = {
            @CacheEvict(value="utilisateur", key="#id_user"),
            @CacheEvict(value="serie", allEntries = true)
    })
    public void deleteUtilisateur(Long id_user) throws ExceptionInterne, ExceptionEntiteNonTrouvee {
        if (!utilisateurRepository.existsById(id_user)) {
            throw new ExceptionEntiteNonTrouvee(Utilisateur.NOM_ENTITE,id_user, "Utilisateur inconnu, suppression impossible.");
        }

        try {
            utilisateurRepository.deleteById(id_user);

        } catch (Exception err) {
            throw new ExceptionInterne("erreur de suppression");
        }

    }


    /**
     * Ajoute une série à un utilisateur
     * @param serie : la serie à ajouter
     * @throws ExceptionInterne : si on échoue à mettre à jour
     * @throws ExceptionEntiteNonTrouvee: si l'utilisateur de la série n'existe pas
     */
    @CacheEvict(value="utilisateur", key="#serie.id_user")
    public void ajouter_serie(Serie serie) throws ExceptionInterne, ExceptionEntiteNonTrouvee {
        if (!utilisateurRepository.existsById(serie.getId_user())) {
            throw new ExceptionEntiteNonTrouvee(Utilisateur.NOM_ENTITE, serie.getId_user(), "Utilisateur inconnu, ajout de la série impossible.");
        }

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
     * @throws ExceptionArgumentIncorrect : si l'identifiant est inconnu ou le mot de passe incorrect
     * @throws ExceptionEntiteNonTrouvee : si on ne trouve pas d'utilisateurs avec ces idenfiants
     */
    public long verifier_identite(String identifiant, String mot_de_passe) throws ExceptionArgumentIncorrect, ExceptionEntiteNonTrouvee {
        Utilisateur user = utilisateurRepository.findByIdentifiant(identifiant);
        if (user == null) {
            throw new ExceptionEntiteNonTrouvee(Utilisateur.NOM_ENTITE, -1L,"Aucun utilisateur ne correspond à cet identifiant.");
        }

        if (BCrypt.checkpw(mot_de_passe, user.getMdp())) {
            return user.getId();
        } else {
            throw new ExceptionArgumentIncorrect("Le mot de passe ne correspond pas à l'identifiant.");
        }
    }


    /**
     * Vérifie que l'utilisateur qui doit accéder a une série existe bien
     * @param serie La serie qui doit être créée
     * @throws ExceptionEntiteNonTrouvee : si l'utilisateur n'existe pas
     */
    public void verifier_existe(Serie serie) throws ExceptionEntiteNonTrouvee {
        if (!utilisateurRepository.existsById(serie.getId_user())) {
            throw new ExceptionEntiteNonTrouvee(Utilisateur.NOM_ENTITE, serie.getId_user(), "Utilisateur inconnu, accès à la série impossible.");
        }
    }

}
