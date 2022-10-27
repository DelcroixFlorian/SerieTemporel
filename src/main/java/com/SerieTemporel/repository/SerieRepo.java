package com.SerieTemporel.repository;

 
import org.springframework.data.jpa.repository.JpaRepository;

import com.SerieTemporel.modele.Serie;
import org.springframework.stereotype.Repository;

@Repository
public interface SerieRepo extends JpaRepository<Serie, Long> {
    
}

