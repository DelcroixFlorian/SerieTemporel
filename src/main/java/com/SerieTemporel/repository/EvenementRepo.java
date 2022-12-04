package com.SerieTemporel.repository;

 
import org.springframework.data.jpa.repository.JpaRepository;

import com.SerieTemporel.modele.Evenement;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository
public interface EvenementRepo extends JpaRepository<Evenement, Long> {

    Iterable<Evenement> getEvenementsByEtiquetteAndIdSerie(String etiquette, Long idSerie);

    Iterable<Evenement> getEvenementsByEtiquetteAndIdSerieOrderByDateDesc(String etiquette, Long idSerie);

    int countEvenementByEtiquetteAndIdSerieAndDateBetween(String etiquette, Long idSerie, Date date_debut, Date date_fin);


}

