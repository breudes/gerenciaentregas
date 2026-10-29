package com.breudes.gerenciaentregas.core.usecases.encomenda;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.breudes.gerenciaentregas.core.domain.Encomenda;
import com.breudes.gerenciaentregas.core.domain.StatusEncomenda;
import com.breudes.gerenciaentregas.core.exception.EncomendaValidationException;
import com.breudes.gerenciaentregas.core.repository.EncomendaRepository;

class ConfirmarNotificacaoDeEncomendaUseCaseTest {

    private static class RepositoryEmMemoria implements EncomendaRepository {
        private final Map<Long, Encomenda> encomendas = new HashMap<>();

        @Override
        public Encomenda criarEncomenda(Encomenda encomenda) {
            return encomenda;
        }

        @Override
        public Encomenda buscarEncomenda(Long idEncomenda) {
            return encomendas.get(idEncomenda);
        }

        @Override
        public Encomenda confirmarEntregaEncomenda(Encomenda encomenda) {
            encomendas.put(encomenda.getId(), encomenda);
            return encomenda;
        }

        @Override
        public boolean confirmaNotificacaoEncomenda(Long idEncomenda) {
            return false;
        }
    }

    @Test
    @DisplayName("Deve confirmar a notificação quando a encomenda for válida.")
    void deveConfirmarNotificacaoQuandoEncomendaForValida() {
        RepositoryEmMemoria repository = new RepositoryEmMemoria();
        Encomenda encomenda = new Encomenda();
        encomenda.setId(1L);
        encomenda.setStatus(StatusEncomenda.NOTIFICACAO_ENVIADA);
        repository.encomendas.put(1L, encomenda);

        ConfirmarNotificacaoDeEncomendaUseCase useCase = new ConfirmarNotificacaoDeEncomendaUseCase(repository);

        Encomenda resultado = useCase.execute(encomenda);

        assertAll(
                () -> assertSame(encomenda, resultado),
                () -> assertSame(encomenda, repository.buscarEncomenda(1L))
        );
    }

    @Test
    @DisplayName("Deve rejeitar confirmação de notificação quando a encomenda for nula.")
    void deveRejeitarConfirmacaoQuandoEncomendaForNula() {
        RepositoryEmMemoria repository = new RepositoryEmMemoria();
        ConfirmarNotificacaoDeEncomendaUseCase useCase = new ConfirmarNotificacaoDeEncomendaUseCase(repository);

        EncomendaValidationException erro = assertThrows(
                EncomendaValidationException.class,
                () -> useCase.execute(null)
        );

        assertEquals("Encomenda não pode ser nula.", erro.getMessage());
    }

    @Test
    @DisplayName("Deve rejeitar confirmação de notificação quando a encomenda não existir.")
    void deveRejeitarConfirmacaoQuandoEncomendaNaoExistir() {
        RepositoryEmMemoria repository = new RepositoryEmMemoria();
        ConfirmarNotificacaoDeEncomendaUseCase useCase = new ConfirmarNotificacaoDeEncomendaUseCase(repository);
        Encomenda encomenda = new Encomenda();
        encomenda.setId(99L);
        encomenda.setStatus(StatusEncomenda.NOTIFICACAO_ENVIADA);

        EncomendaValidationException erro = assertThrows(
                EncomendaValidationException.class,
                () -> useCase.execute(encomenda)
        );

        assertEquals("Encomenda não encontrada no sistema.", erro.getMessage());
    }

    @Test
    @DisplayName("Deve rejeitar confirmação de notificação quando o status for nulo.")
    void deveRejeitarConfirmacaoQuandoStatusForNulo() {
        RepositoryEmMemoria repository = new RepositoryEmMemoria();
        Encomenda encomenda = new Encomenda();
        encomenda.setId(1L);
        encomenda.setStatus(null);
        repository.encomendas.put(1L, encomenda);

        ConfirmarNotificacaoDeEncomendaUseCase useCase = new ConfirmarNotificacaoDeEncomendaUseCase(repository);

        EncomendaValidationException erro = assertThrows(
                EncomendaValidationException.class,
                () -> useCase.execute(encomenda)
        );

        assertEquals("Status da encomenda não pode ser nulo.", erro.getMessage());
    }

    @Test
    @DisplayName("Deve rejeitar confirmação de notificação quando o status for inválido.")
    void deveRejeitarConfirmacaoQuandoStatusForInvalido() {
        RepositoryEmMemoria repository = new RepositoryEmMemoria();
        Encomenda encomenda = new Encomenda();
        encomenda.setId(1L);
        encomenda.setStatus(StatusEncomenda.PENDENTE);
        repository.encomendas.put(1L, encomenda);

        ConfirmarNotificacaoDeEncomendaUseCase useCase = new ConfirmarNotificacaoDeEncomendaUseCase(repository);

        EncomendaValidationException erro = assertThrows(
                EncomendaValidationException.class,
                () -> useCase.execute(encomenda)
        );

        assertEquals("Notificação ainda não foi enviada.", erro.getMessage());
    }
}
