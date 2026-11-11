package com.breudes.gerenciaentregas.interfaces.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.breudes.gerenciaentregas.core.domain.Morador;
import com.breudes.gerenciaentregas.core.usecases.morador.AtualizarMoradorUseCase;
import com.breudes.gerenciaentregas.core.usecases.morador.CriarMoradorUseCase;
import com.breudes.gerenciaentregas.interfaces.dto.morador.MoradorOutputDTO;
import com.breudes.gerenciaentregas.interfaces.dto.morador.MoradorRequestDTO;
import org.junit.jupiter.api.Test;

class MoradorControllerTest {

    private final CriarMoradorUseCase criarMoradorUseCase = mock(CriarMoradorUseCase.class);
    private final AtualizarMoradorUseCase atualizarMoradorUseCase = mock(AtualizarMoradorUseCase.class);
    private final MoradorController controller = new MoradorController(criarMoradorUseCase, atualizarMoradorUseCase);

    @Test
    void deveCriarMorador() {
        MoradorRequestDTO request = new MoradorRequestDTO("Maria", "11999999999", "101", "A");
        Morador moradorEsperado = new Morador(1L, "Maria", "11999999999", "101", "A");
        when(criarMoradorUseCase.execute(any(Morador.class))).thenReturn(moradorEsperado);

        Morador resultado = controller.criaMorador(request);

        assertSame(moradorEsperado, resultado);
        verify(criarMoradorUseCase).execute(any(Morador.class));
    }

    @Test
    void deveAtualizarMorador() {
        MoradorOutputDTO output = new MoradorOutputDTO("Maria Atualizada", "11999999999", "202", "B");
        Morador moradorEsperado = new Morador(1L, "Maria Atualizada", "11999999999", "202", "B");
        when(atualizarMoradorUseCase.execute(eq("11999999999"), any(Morador.class))).thenReturn(moradorEsperado);

        Morador resultado = controller.atualizaMorador("11999999999", output);

        assertEquals("Maria Atualizada", resultado.getNome());
        verify(atualizarMoradorUseCase).execute(eq("11999999999"), any(Morador.class));
    }
}
