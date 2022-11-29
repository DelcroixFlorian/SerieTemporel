package com.SerieTemporel.Service;

import java.util.ArrayList;
import java.util.List;

import com.SerieTemporel.exception.ExceptionFormatObjetInvalide;
import com.SerieTemporel.exception.ExceptionInterne;
import com.SerieTemporel.modele.Serie;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.SerieTemporel.modele.Utilisateur;
import com.SerieTemporel.repository.UtilisateurRepo;

@Service
public class UtilisateurService {
    
    @Autowired
    UtilisateurRepo utilisateurRepository;

    @Autowired
    SerieService serieService;

    public List getAllUtilisateurs() throws ExceptionInterne {
        try {
            List utilisateurs = new ArrayList();
            utilisateurRepository.findAll().forEach(utilisateur -> utilisateurs.add(utilisateur));
            return utilisateurs;
        } catch (Exception err) {
            throw  new ExceptionInterne("erreur de récupération des données");
        }
    }

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

    public void ajouter_serie(Serie serie) throws ExceptionInterne {
        try {
            utilisateurRepository.findById(serie.getId_user()).ifPresent(user -> user.ajouter_serie(serie));
        } catch (Exception err) {
            throw new ExceptionInterne("erreur d'ajout de la serie à l'utilisateur");
        }
    }


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

    public void verifier_existe(Serie serie) throws ExceptionFormatObjetInvalide {
        if (!utilisateurRepository.existsById(serie.getId_user())) {
            throw new ExceptionFormatObjetInvalide("Utilisateur inconnu, création de la série impossible.");
        }
    }

}
