package com.breudes.gerenciaentregas.interfaces.handler;

import com.breudes.gerenciaentregas.core.exception.EncomendaValidationException;
import com.breudes.gerenciaentregas.core.exception.MoradorValidationException;
import com.breudes.gerenciaentregas.interfaces.dto.ApiErrorDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(MoradorValidationException.class)
    public ResponseEntity<ApiErrorDTO> handleMoradorValidationException(MoradorValidationException exception) {
        return ResponseEntity.badRequest().body(new ApiErrorDTO(exception.getAtributo(), exception.getMessage()));
    }

    @ExceptionHandler(MoradorValidationException.class)
    public ResponseEntity<ApiErrorDTO> handleEncomendaValidationException(EncomendaValidationException exception) {
        return ResponseEntity.badRequest().body(new ApiErrorDTO(exception.getAtributo(), exception.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiErrorDTO> handleIllegalArgumentException(IllegalArgumentException exception) {
        return ResponseEntity.badRequest().body(new ApiErrorDTO("Erro", exception.getMessage()));
    }
}
