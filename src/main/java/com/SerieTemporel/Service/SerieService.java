package com.SerieTemporel.Service;

import com.SerieTemporel.modele.Evenement;
import com.SerieTemporel.modele.Serie;
import com.SerieTemporel.repository.SerieRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SerieService {

    @Autowired
    SerieRepo serieRepo;

    public long creer_serie(Serie new_serie) {
        Serie serie =  serieRepo.save(new_serie);
        return serie.getId();
    }

    public Serie get_info_serie(long id) {
        return serieRepo.findById(id).orElse(null);
    }

}
