package com.breudes.gerenciaentregas.core.exception;

public class MoradorValidationException extends RuntimeException {
    private final String atributo;

    public MoradorValidationException(String atributo, String message) {
        super(message);
        this.atributo = atributo;
    }

    public String getAtributo() {
        return atributo;
    }
}
