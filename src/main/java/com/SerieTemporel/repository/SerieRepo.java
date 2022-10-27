package com.SerieTemporel.repository;

 
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.SerieTemporel.modele.Serie;
 
@Repository
public interface SerieRepo extends JpaRepository<Serie, Integer> {
    
}

