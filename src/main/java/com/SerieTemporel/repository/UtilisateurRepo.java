package com.SerieTemporel.repository;

 
import org.springframework.data.jpa.repository.JpaRepository;

import com.SerieTemporel.modele.Utilisateur;
 
public interface UtilisateurRepo extends JpaRepository<Utilisateur, Integer> {
    
}
