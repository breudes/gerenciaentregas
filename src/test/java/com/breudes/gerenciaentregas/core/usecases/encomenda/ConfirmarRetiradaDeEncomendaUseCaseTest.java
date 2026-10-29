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

class ConfirmarRetiradaDeEncomendaUseCaseTest {

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
    @DisplayName("Deve confirmar a retirada quando a encomenda for válida.")
    void deveConfirmarRetiradaQuandoEncomendaForValida() {
        RepositoryEmMemoria repository = new RepositoryEmMemoria();
        Encomenda encomenda = new Encomenda();
        encomenda.setId(1L);
        encomenda.setStatus(StatusEncomenda.NOTIFICACAO_ENVIADA);
        repository.encomendas.put(1L, encomenda);

        ConfirmarRetiradaDeEncomendaUseCase useCase = new ConfirmarRetiradaDeEncomendaUseCase(repository);

        Encomenda resultado = useCase.execute(encomenda);

        assertAll(
                () -> assertSame(encomenda, resultado),
                () -> assertSame(encomenda, repository.buscarEncomenda(1L))
        );
    }

    @Test
    @DisplayName("Deve rejeitar confirmação quando a encomenda for nula.")
    void deveRejeitarConfirmacaoQuandoEncomendaForNula() {
        RepositoryEmMemoria repository = new RepositoryEmMemoria();
        ConfirmarRetiradaDeEncomendaUseCase useCase = new ConfirmarRetiradaDeEncomendaUseCase(repository);

        EncomendaValidationException erro = assertThrows(
                EncomendaValidationException.class,
                () -> useCase.execute(null)
        );

        assertEquals("Encomenda não pode ser nula.", erro.getMessage());
    }

    @Test
    @DisplayName("Deve rejeitar confirmação quando a encomenda não existir.")
    void deveRejeitarConfirmacaoQuandoEncomendaNaoExistir() {
        RepositoryEmMemoria repository = new RepositoryEmMemoria();
        ConfirmarRetiradaDeEncomendaUseCase useCase = new ConfirmarRetiradaDeEncomendaUseCase(repository);
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
    @DisplayName("Deve rejeitar confirmação quando o status for nulo.")
    void deveRejeitarConfirmacaoQuandoStatusForNulo() {
        RepositoryEmMemoria repository = new RepositoryEmMemoria();
        Encomenda encomenda = new Encomenda();
        encomenda.setId(1L);
        encomenda.setStatus(null);
        repository.encomendas.put(1L, encomenda);

        ConfirmarRetiradaDeEncomendaUseCase useCase = new ConfirmarRetiradaDeEncomendaUseCase(repository);

        EncomendaValidationException erro = assertThrows(
                EncomendaValidationException.class,
                () -> useCase.execute(encomenda)
        );

        assertEquals("Status da encomenda não pode ser nulo.", erro.getMessage());
    }

    @Test
    @DisplayName("Deve rejeitar confirmação quando o status for inválido.")
    void deveRejeitarConfirmacaoQuandoStatusForInvalido() {
        RepositoryEmMemoria repository = new RepositoryEmMemoria();
        Encomenda encomenda = new Encomenda();
        encomenda.setId(1L);
        encomenda.setStatus(StatusEncomenda.PENDENTE);
        repository.encomendas.put(1L, encomenda);

        ConfirmarRetiradaDeEncomendaUseCase useCase = new ConfirmarRetiradaDeEncomendaUseCase(repository);

        EncomendaValidationException erro = assertThrows(
                EncomendaValidationException.class,
                () -> useCase.execute(encomenda)
        );

        assertEquals("Encomenda ainda não foi notificada ao destinatário, não é possível confirmar a retirada.", erro.getMessage());
    }
}
