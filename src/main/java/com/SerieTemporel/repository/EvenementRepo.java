package com.SerieTemporel.repository;

 
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.SerieTemporel.modele.Evenement;
 
@Repository
public interface EvenementRepo extends JpaRepository<Evenement, Integer> {
    
}

