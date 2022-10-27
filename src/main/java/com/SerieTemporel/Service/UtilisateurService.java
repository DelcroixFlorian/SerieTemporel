package com.SerieTemporel.Service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.SerieTemporel.modele.Utilisateur;
import com.SerieTemporel.repository.UtilisateurRepo;

@Service
public class UtilisateurService {
    
    @Autowired
    UtilisateurRepo utilisateurRepository;

    public List getAllUtilisateurs() {
        List utilisateurs = new ArrayList();
        utilisateurRepository.findAll().forEach(utilisateur -> utilisateurs.add(utilisateur));
        return utilisateurs;
    }

    public Utilisateur getUtilisateur(Long userid) {
        return utilisateurRepository.findById(userid).get();
    }

    public Long creerUtilisateur(Utilisateur user) {
        Utilisateur new_user = utilisateurRepository.save(user);
        return new_user.getId();
    }

    public void deleteUtilisateur(Long userid){
        utilisateurRepository.deleteById(userid);
    }
}
