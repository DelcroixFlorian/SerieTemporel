package com.SerieTemporel.controller;

import com.SerieTemporel.repository.SerieRepo;

public class SerieController {
    private final SerieRepo repo;
    
    public SerieController(SerieRepo repo){
        this.repo = repo;
    }
}
