package com.breudes.gerenciaentregas.interfaces.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.breudes.gerenciaentregas.core.domain.Encomenda;
import com.breudes.gerenciaentregas.core.domain.Morador;
import com.breudes.gerenciaentregas.core.domain.StatusEncomenda;
import com.breudes.gerenciaentregas.core.usecases.encomenda.ConfirmarNotificacaoDeEncomendaUseCase;
import com.breudes.gerenciaentregas.core.usecases.encomenda.ConfirmarRetiradaDeEncomendaUseCase;
import com.breudes.gerenciaentregas.core.usecases.encomenda.CriarEncomendaUseCase;
import com.breudes.gerenciaentregas.interfaces.dto.encomenda.EncomendaRequestDto;
import com.breudes.gerenciaentregas.interfaces.dto.morador.MoradorRequestDTO;
import org.junit.jupiter.api.Test;

class EncomendaControllerTest {

    private final CriarEncomendaUseCase criarEncomendaUseCase = mock(CriarEncomendaUseCase.class);
    private final ConfirmarNotificacaoDeEncomendaUseCase confirmarNotificacaoDeEncomendaUseCase = mock(ConfirmarNotificacaoDeEncomendaUseCase.class);
    private final ConfirmarRetiradaDeEncomendaUseCase confirmarRetiradaDeEncomendaUseCase = mock(ConfirmarRetiradaDeEncomendaUseCase.class);
    private final EncomendaController controller = new EncomendaController(
            criarEncomendaUseCase,
            confirmarNotificacaoDeEncomendaUseCase,
            confirmarRetiradaDeEncomendaUseCase
    );

    @Test
    void deveCriarEncomenda() {
        EncomendaRequestDto request = new EncomendaRequestDto("Livro de Java",
                new MoradorRequestDTO("Maria", "11999999999", "101", "A"));
        Encomenda encomendaEsperada = new Encomenda(1L, "Livro de Java",
                new Morador(1L, "Maria", "11999999999", "101", "A"), null, null, null, true, StatusEncomenda.PENDENTE);
        when(criarEncomendaUseCase.execute(any(Encomenda.class))).thenReturn(encomendaEsperada);

        Encomenda resultado = controller.criarEncomenda(request);

        assertEquals("Livro de Java", resultado.getDescricao());
        verify(criarEncomendaUseCase).execute(any(Encomenda.class));
    }

    @Test
    void deveConfirmarNotificacao() {
        when(confirmarNotificacaoDeEncomendaUseCase.execute(1L)).thenReturn(new Encomenda());

        boolean resultado = controller.confirmaNotificacaoEncomenda(1L);

        assertTrue(resultado);
        verify(confirmarNotificacaoDeEncomendaUseCase).execute(1L);
    }

    @Test
    void deveRetornarFalsoQuandoNotificacaoNaoForConfirmada() {
        when(confirmarNotificacaoDeEncomendaUseCase.execute(1L)).thenReturn(null);

        boolean resultado = controller.confirmaNotificacaoEncomenda(1L);

        assertFalse(resultado);
        verify(confirmarNotificacaoDeEncomendaUseCase).execute(1L);
    }

    @Test
    void deveConfirmarRetirada() {
        when(confirmarRetiradaDeEncomendaUseCase.execute(any(Encomenda.class))).thenReturn(new Encomenda());

        boolean resultado = controller.confirmaRetiradaEncomenda(2L);

        assertTrue(resultado);
        verify(confirmarRetiradaDeEncomendaUseCase).execute(any(Encomenda.class));
    }

    @Test
    void deveRetornarFalsoQuandoRetiradaNaoForConfirmada() {
        when(confirmarRetiradaDeEncomendaUseCase.execute(any(Encomenda.class))).thenReturn(null);

        boolean resultado = controller.confirmaRetiradaEncomenda(2L);

        assertFalse(resultado);
        verify(confirmarRetiradaDeEncomendaUseCase).execute(any(Encomenda.class));
    }
}
