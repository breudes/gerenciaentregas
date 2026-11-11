package com.breudes.gerenciaentregas.interfaces.handler;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.breudes.gerenciaentregas.core.exception.EncomendaValidationException;
import com.breudes.gerenciaentregas.core.exception.MoradorValidationException;
import com.breudes.gerenciaentregas.interfaces.dto.ApiErrorDTO;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

class ApiExceptionHandlerTest {

    private final ApiExceptionHandler handler = new ApiExceptionHandler();

    @Test
    void deveTratarExcecaoDeValidacaoDeMorador() {
        ResponseEntity<ApiErrorDTO> response = handler.handleMoradorValidationException(
                new MoradorValidationException("telefone", "Telefone obrigatório")
        );

        assertAll(
                () -> assertEquals(400, response.getStatusCode().value()),
                () -> assertEquals(new ApiErrorDTO("telefone", "Telefone obrigatório"), response.getBody())
        );
    }

    @Test
    void deveTratarExcecaoDeValidacaoDeEncomenda() {
        ResponseEntity<ApiErrorDTO> response = handler.handleEncomendaValidationException(
                new EncomendaValidationException("descricao", "Descrição obrigatória")
        );

        assertAll(
                () -> assertEquals(400, response.getStatusCode().value()),
                () -> assertEquals(new ApiErrorDTO("descricao", "Descrição obrigatória"), response.getBody())
        );
    }

    @Test
    void deveTratarIllegalArgumentException() {
        ResponseEntity<ApiErrorDTO> response = handler.handleIllegalArgumentException(
                new IllegalArgumentException("Dado inválido")
        );

        assertAll(
                () -> assertEquals(400, response.getStatusCode().value()),
                () -> assertEquals(new ApiErrorDTO("Erro", "Dado inválido"), response.getBody())
        );
    }
}
