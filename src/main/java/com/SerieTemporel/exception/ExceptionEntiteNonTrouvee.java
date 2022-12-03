package com.SerieTemporel.exception;

public class ExceptionEntiteNonTrouvee extends Exception {
    public ExceptionEntiteNonTrouvee(String nom_entite, Long identifiant, String commentaire) {
        super("L'entité '" + nom_entite +
              "' identifié par le numéro " + identifiant +
              " n'a pas été trouvée. (Elle n'existe probablement pas). " +
              commentaire);
    }
}
