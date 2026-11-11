package com.breudes.gerenciaentregas.core.exception;

public class EncomendaValidationException extends IllegalArgumentException {
    private final String atributo;

    public EncomendaValidationException(String atributo, String message) {
        super(message);
        this.atributo = atributo;
    }

    public String getAtributo() {
        return atributo;
    }
}
