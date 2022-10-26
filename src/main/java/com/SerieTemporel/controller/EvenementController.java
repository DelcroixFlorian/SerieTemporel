package com.SerieTemporel.controller;

import com.SerieTemporel.repository.EvenementRepo;

public class EvenementController {
    private final EvenementRepo repo;
    
    public EvenementController(EvenementRepo repo){
        this.repo = repo;
    }
}
