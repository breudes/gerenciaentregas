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

class EnviaNotificacaoDeEncomendaUseCaseTest {

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
        public Encomenda enviaNotificacaoEncomenda(Long idEncomenda) {
            Encomenda encomenda = encomendas.get(idEncomenda);
            if (encomenda != null) {
                encomenda.setStatus(StatusEncomenda.NOTIFICACAO_ENVIADA);
                encomendas.put(idEncomenda, encomenda);
            }
            return encomenda;
        }

        @Override
        public Encomenda confirmaNotificacaoEncomenda(Long idEncomenda) {
            return null;
        }
    }

    @Test
    @DisplayName("Deve enviar a notificação quando a encomenda for válida.")
    void deveEnviarNotificacaoQuandoEncomendaForValida() {
        RepositoryEmMemoria repository = new RepositoryEmMemoria();
        Encomenda encomenda = new Encomenda();
        encomenda.setId(1L);
        encomenda.setStatus(StatusEncomenda.PENDENTE);
        repository.encomendas.put(1L, encomenda);

        EnviaNotificacaoDeEncomendaUseCase useCase = new EnviaNotificacaoDeEncomendaUseCase(repository);

        Encomenda resultado = useCase.execute(1L);

        assertAll(
                () -> assertSame(encomenda, resultado),
                () -> assertEquals(StatusEncomenda.NOTIFICACAO_ENVIADA, repository.buscarEncomenda(1L).getStatus())
        );
    }

    @Test
    @DisplayName("Deve rejeitar envio de notificação quando o id da encomenda for nulo.")
    void deveRejeitarEnvioQuandoIdForNulo() {
        RepositoryEmMemoria repository = new RepositoryEmMemoria();
        EnviaNotificacaoDeEncomendaUseCase useCase = new EnviaNotificacaoDeEncomendaUseCase(repository);

        EncomendaValidationException erro = assertThrows(
                EncomendaValidationException.class,
                () -> useCase.execute(null)
        );

        assertEquals("ID de encomenda não pode ser nulo.", erro.getMessage());
    }

    @Test
    @DisplayName("Deve rejeitar envio de notificação quando a encomenda não existir.")
    void deveRejeitarEnvioQuandoEncomendaNaoExistir() {
        RepositoryEmMemoria repository = new RepositoryEmMemoria();
        EnviaNotificacaoDeEncomendaUseCase useCase = new EnviaNotificacaoDeEncomendaUseCase(repository);

        EncomendaValidationException erro = assertThrows(
                EncomendaValidationException.class,
                () -> useCase.execute(99L)
        );

        assertEquals("Encomenda não encontrada no sistema.", erro.getMessage());
    }

    @Test
    @DisplayName("Deve rejeitar envio de notificação quando o status for nulo.")
    void deveRejeitarEnvioQuandoStatusForNulo() {
        RepositoryEmMemoria repository = new RepositoryEmMemoria();
        Encomenda encomenda = new Encomenda();
        encomenda.setId(1L);
        encomenda.setStatus(null);
        repository.encomendas.put(1L, encomenda);

        EnviaNotificacaoDeEncomendaUseCase useCase = new EnviaNotificacaoDeEncomendaUseCase(repository);

        EncomendaValidationException erro = assertThrows(
                EncomendaValidationException.class,
                () -> useCase.execute(1L)
        );

        assertEquals("Status da encomenda não pode ser nulo.", erro.getMessage());
    }
}
