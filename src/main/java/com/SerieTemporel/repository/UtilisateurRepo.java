package com.SerieTemporel.repository;

 
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.SerieTemporel.modele.Utilisateur;
 
@Repository
public interface UtilisateurRepo extends JpaRepository<Utilisateur, Long> {
    
}
