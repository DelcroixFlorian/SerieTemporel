package com.SerieTemporel.repository;

 
import org.springframework.data.jpa.repository.JpaRepository;

import com.SerieTemporel.modele.Serie;
 
public interface SerieRepo extends JpaRepository<Serie, Integer> {
    
}

