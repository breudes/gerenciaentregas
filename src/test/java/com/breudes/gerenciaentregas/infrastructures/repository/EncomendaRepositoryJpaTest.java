package com.breudes.gerenciaentregas.infrastructures.repository;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;

import com.breudes.gerenciaentregas.core.domain.Encomenda;
import com.breudes.gerenciaentregas.core.domain.Morador;
import com.breudes.gerenciaentregas.core.domain.StatusEncomenda;
import com.breudes.gerenciaentregas.infrastructures.entity.EncomendaJpa;
import com.breudes.gerenciaentregas.infrastructures.entity.MoradorJpa;
import com.breudes.gerenciaentregas.infrastructures.enums.StatusEncomendaJpa;
import com.breudes.gerenciaentregas.infrastructures.springdatajpa.SpringDataJpaEncomenda;
import com.breudes.gerenciaentregas.infrastructures.springdatajpa.SpringDataJpaMorador;
import org.junit.jupiter.api.Test;

class EncomendaRepositoryJpaTest {

    private final SpringDataJpaEncomenda springDataJpaEncomenda = mock(SpringDataJpaEncomenda.class);
    private final SpringDataJpaMorador springDataJpaMorador = mock(SpringDataJpaMorador.class);
    private final EncomendaRepositoryJpa repository = new EncomendaRepositoryJpa(springDataJpaEncomenda, springDataJpaMorador);

    @Test
    void deveCriarEncomendaQuandoDescricaoNaoExiste() {
        Morador moradorDestinatario = new Morador(1L, "Maria Silva", "11999999999", "101", "A");
        Encomenda encomenda = new Encomenda(null, "Livro de Java", moradorDestinatario, null, LocalDateTime.now(), null, true, StatusEncomenda.PENDENTE);

        when(springDataJpaEncomenda.findByDescricao("Livro de Java")).thenReturn(Optional.empty());
        when(springDataJpaMorador.findByTelefone("11999999999")).thenReturn(Optional.of(new MoradorJpa(1L, "Maria Silva", "11999999999", "101", "A")));
        when(springDataJpaEncomenda.save(any(EncomendaJpa.class))).thenAnswer(invocation -> {
            EncomendaJpa encomendaJpa = invocation.getArgument(0);
            encomendaJpa.setId(10L);
            return encomendaJpa;
        });

        Encomenda resultado = repository.criarEncomenda(encomenda);

        assertAll(
                () -> assertEquals(10L, resultado.getId()),
                () -> assertEquals("Livro de Java", resultado.getDescricao()),
                () -> assertEquals(StatusEncomenda.PENDENTE, resultado.getStatus())
        );
    }

    @Test
    void deveLancarExcecaoQuandoDescricaoJaExiste() {
        Morador moradorDestinatario = new Morador(1L, "Maria Silva", "11999999999", "101", "A");
        Encomenda encomenda = new Encomenda(null, "Livro de Java", moradorDestinatario, null, LocalDateTime.now(), null, true, StatusEncomenda.PENDENTE);

        when(springDataJpaEncomenda.findByDescricao("Livro de Java")).thenReturn(Optional.of(new EncomendaJpa()));

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> repository.criarEncomenda(encomenda)
        );

        assertEquals("Encomenda já registrada", exception.getMessage());
    }

    @Test
    void deveLancarExcecaoQuandoTelefoneJaEstiverRegistradoParaOutroMorador() {
        Morador moradorDestinatario = new Morador(1L, "Maria Silva", "11999999999", "101", "A");
        Encomenda encomenda = new Encomenda(null, "Livro de Java", moradorDestinatario, null, LocalDateTime.now(), null, true, StatusEncomenda.PENDENTE);

        when(springDataJpaEncomenda.findByDescricao("Livro de Java")).thenReturn(Optional.empty());
        when(springDataJpaMorador.findByTelefone("11999999999")).thenReturn(Optional.of(new MoradorJpa(2L, "João Pereira", "11999999999", "202", "B")));

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> repository.criarEncomenda(encomenda)
        );

        assertEquals("Telefone já registrado para outro morador.", exception.getMessage());
    }

    @Test
    void deveRetornarNullQuandoMoradorNaoForEncontradoAoCriarEncomenda() {
        Morador moradorDestinatario = new Morador(1L, "Maria Silva", "11999999999", "101", "A");
        Encomenda encomenda = new Encomenda(null, "Livro de Java", moradorDestinatario, null, LocalDateTime.now(), null, true, StatusEncomenda.PENDENTE);

        when(springDataJpaEncomenda.findByDescricao("Livro de Java")).thenReturn(Optional.empty());
        when(springDataJpaMorador.findByTelefone("11999999999")).thenReturn(Optional.empty());

        Encomenda resultado = repository.criarEncomenda(encomenda);

        assertNull(resultado);
    }

    @Test
    void deveBuscarEncomendaPorId() {
        EncomendaJpa encomendaJpa = new EncomendaJpa(7L, "Livro de Java",
                new MoradorJpa(1L, "Maria Silva", "11999999999", "101", "A"), null,
                LocalDateTime.now(), null, true, StatusEncomendaJpa.PENDENTE);
        when(springDataJpaEncomenda.findById(7L)).thenReturn(Optional.of(encomendaJpa));

        Encomenda resultado = repository.buscarEncomenda(7L);

        assertNotNull(resultado);
        assertEquals("Livro de Java", resultado.getDescricao());
        assertEquals(StatusEncomenda.PENDENTE, resultado.getStatus());
    }

    @Test
    void deveRetornarNullQuandoEncomendaForNula() {
        assertNull(repository.criarEncomenda(null));
    }

    @Test
    void deveRetornarNullQuandoBuscarEncomendaComIdNulo() {
        assertNull(repository.buscarEncomenda(null));
    }

    @Test
    void deveRetornarNullQuandoEncomendaNaoExistirParaBusca() {
        when(springDataJpaEncomenda.findById(99L)).thenReturn(Optional.empty());

        assertNull(repository.buscarEncomenda(99L));
    }

    @Test
    void deveRetornarNullQuandoEnviarNotificacaoComIdNulo() {
        assertNull(repository.enviaNotificacaoEncomenda(null));
    }

    @Test
    void deveRetornarNullQuandoEncomendaNaoExistirParaEnviarNotificacao() {
        when(springDataJpaEncomenda.findById(99L)).thenReturn(Optional.empty());

        assertNull(repository.enviaNotificacaoEncomenda(99L));
    }

    @Test
    void deveRetornarNullQuandoStatusNaoPermiteEnviarNotificacao() {
        EncomendaJpa encomendaJpa = new EncomendaJpa(7L, "Livro de Java",
                new MoradorJpa(1L, "Maria Silva", "11999999999", "101", "A"), null,
                LocalDateTime.now(), null, true, StatusEncomendaJpa.NOTIFICACAO_ENVIADA);
        when(springDataJpaEncomenda.findById(7L)).thenReturn(Optional.of(encomendaJpa));

        assertNull(repository.enviaNotificacaoEncomenda(7L));
    }

    @Test
    void deveRetornarNullQuandoConfirmarNotificacaoComIdNulo() {
        assertNull(repository.confirmaNotificacaoEncomenda(null));
    }

    @Test
    void deveRetornarNullQuandoEncomendaNaoExistirParaConfirmarNotificacao() {
        when(springDataJpaEncomenda.findById(99L)).thenReturn(Optional.empty());

        assertNull(repository.confirmaNotificacaoEncomenda(99L));
    }

    @Test
    void deveRetornarNullQuandoStatusNaoPermiteConfirmarNotificacao() {
        EncomendaJpa encomendaJpa = new EncomendaJpa(7L, "Livro de Java",
                new MoradorJpa(1L, "Maria Silva", "11999999999", "101", "A"), null,
                LocalDateTime.now(), null, true, StatusEncomendaJpa.PENDENTE);
        when(springDataJpaEncomenda.findById(7L)).thenReturn(Optional.of(encomendaJpa));

        assertNull(repository.confirmaNotificacaoEncomenda(7L));
    }

    @Test
    void deveRetornarNullQuandoConfirmarEntregaComEncomendaNula() {
        assertNull(repository.confirmarEntregaEncomenda(null));
    }

    @Test
    void deveRetornarNullQuandoConfirmarEntregaSemId() {
        Encomenda encomenda = new Encomenda(null, "Livro de Java",
                new Morador(1L, "Maria Silva", "11999999999", "101", "A"), null,
                LocalDateTime.now(), null, true, StatusEncomenda.PENDENTE);

        assertNull(repository.confirmarEntregaEncomenda(encomenda));
    }

    @Test
    void deveRetornarNullQuandoConfirmarEntregaComEncomendaInexistente() {
        Encomenda encomenda = new Encomenda(99L, "Livro de Java",
                new Morador(1L, "Maria Silva", "11999999999", "101", "A"), null,
                LocalDateTime.now(), null, true, StatusEncomenda.PENDENTE);
        when(springDataJpaEncomenda.findById(99L)).thenReturn(Optional.empty());

        assertNull(repository.confirmarEntregaEncomenda(encomenda));
    }

    @Test
    void deveEnviarNotificacaoQuandoStatusEstiverPendente() {
        EncomendaJpa encomendaJpa = new EncomendaJpa(7L, "Livro de Java",
                new MoradorJpa(1L, "Maria Silva", "11999999999", "101", "A"), null,
                LocalDateTime.now(), null, true, StatusEncomendaJpa.PENDENTE);
        when(springDataJpaEncomenda.findById(7L)).thenReturn(Optional.of(encomendaJpa));
        when(springDataJpaEncomenda.save(any(EncomendaJpa.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Encomenda resultado = repository.enviaNotificacaoEncomenda(7L);

        assertAll(
                () -> assertEquals(StatusEncomenda.NOTIFICACAO_ENVIADA, resultado.getStatus()),
                () -> assertFalse(resultado.getStatus() == StatusEncomenda.PENDENTE)
        );
    }

    @Test
    void deveConfirmarNotificacaoQuandoStatusEstiverEnviado() {
        EncomendaJpa encomendaJpa = new EncomendaJpa(7L, "Livro de Java",
                new MoradorJpa(1L, "Maria Silva", "11999999999", "101", "A"), null,
                LocalDateTime.now(), null, true, StatusEncomendaJpa.NOTIFICACAO_ENVIADA);
        when(springDataJpaEncomenda.findById(7L)).thenReturn(Optional.of(encomendaJpa));
        when(springDataJpaEncomenda.save(any(EncomendaJpa.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Encomenda resultado = repository.confirmaNotificacaoEncomenda(7L);

        assertEquals(StatusEncomenda.NOTIFICACAO_RECEBIDA, resultado.getStatus());
    }

    @Test
    void deveConfirmarEntregaQuandoEncomendaExistir() {
        EncomendaJpa encomendaJpa = new EncomendaJpa(7L, "Livro de Java",
                new MoradorJpa(1L, "Maria Silva", "11999999999", "101", "A"), null,
                LocalDateTime.now(), null, true, StatusEncomendaJpa.NOTIFICACAO_RECEBIDA);
        when(springDataJpaEncomenda.findById(7L)).thenReturn(Optional.of(encomendaJpa));
        when(springDataJpaEncomenda.save(any(EncomendaJpa.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Encomenda encomenda = new Encomenda(7L, "Livro de Java", new Morador(1L, "Maria Silva", "11999999999", "101", "A"), null,
                LocalDateTime.now(), null, true, StatusEncomenda.NOTIFICACAO_RECEBIDA);

        Encomenda resultado = repository.confirmarEntregaEncomenda(encomenda);

        assertAll(
                () -> assertEquals(StatusEncomenda.ENTREGUE, resultado.getStatus()),
                () -> assertFalse(resultado.isAtivo())
        );
    }
}
