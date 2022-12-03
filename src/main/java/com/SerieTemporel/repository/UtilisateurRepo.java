package com.SerieTemporel.repository;

 
import org.springframework.data.jpa.repository.JpaRepository;

import com.SerieTemporel.modele.Utilisateur;
import org.springframework.stereotype.Repository;


@Repository
public interface UtilisateurRepo extends JpaRepository<Utilisateur, Long> {

    Utilisateur findByIdentifiant(String identifiant);
}
