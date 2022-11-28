package com.SerieTemporel.Service;

import java.util.ArrayList;
import java.util.List;

import com.SerieTemporel.exception.ExceptionFormatObjetInvalide;
import com.SerieTemporel.exception.ExceptionInterne;
import com.SerieTemporel.modele.Serie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.SerieTemporel.modele.Utilisateur;
import com.SerieTemporel.repository.UtilisateurRepo;

@Service
public class UtilisateurService {
    
    @Autowired
    UtilisateurRepo utilisateurRepository;

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

    public void ajouter_serie(long id_user, Serie serie) throws ExceptionFormatObjetInvalide, ExceptionInterne {
        if (!utilisateurRepository.existsById(id_user)) {
            throw new ExceptionFormatObjetInvalide("Utilisateur inconnu, ajout de la série impossible.");
        }
        try {
            Utilisateur user = utilisateurRepository.findById(id_user).orElse(null);
            if (user != null && !user.ajouter_serie(serie)) {
                throw new ExceptionInterne("");
            }
        } catch (Exception err) {
            throw new ExceptionInterne("erreur de suppression");
        }
    }
}
