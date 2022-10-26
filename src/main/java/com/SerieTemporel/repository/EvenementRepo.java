package com.SerieTemporel.repository;

 
import org.springframework.data.jpa.repository.JpaRepository;

import com.SerieTemporel.modele.Evenement;
 
public interface EvenementRepo extends JpaRepository<Evenement, Integer> {
    
}

