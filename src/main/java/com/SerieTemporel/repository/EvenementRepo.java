package com.SerieTemporel.repository;

 
import org.springframework.data.jpa.repository.JpaRepository;

import com.SerieTemporel.modele.Evenement;
import org.springframework.stereotype.Repository;

@Repository
public interface EvenementRepo extends JpaRepository<Evenement, Long> {

    Iterable<Evenement> getEvenementsByEtiquetteAndIdSerie(String etiquette, Long idSerie);

    Evenement findEvenementByEtiquetteAndIdSerieOrderByDateDesc(String etiquette, Long idSerie);

}

