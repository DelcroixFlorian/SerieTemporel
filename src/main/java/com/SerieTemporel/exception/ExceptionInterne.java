package com.SerieTemporel.exception;

public class ExceptionInterne extends Exception {
    private static final String MESSAGE_ERREUR_INTERNE = "Une erreur interne au serveur est survenue lors du traitement de l'opération, veuillez réessayer. Détail : ";

    public ExceptionInterne(String message) {
        super(MESSAGE_ERREUR_INTERNE + message);
    }
}
