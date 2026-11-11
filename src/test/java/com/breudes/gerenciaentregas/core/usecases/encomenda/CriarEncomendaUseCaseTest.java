package com.breudes.gerenciaentregas.core.usecases.encomenda;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.breudes.gerenciaentregas.core.domain.Encomenda;
import com.breudes.gerenciaentregas.core.domain.Morador;
import com.breudes.gerenciaentregas.core.domain.StatusEncomenda;
import com.breudes.gerenciaentregas.core.event.NotificacaoEmail;
import com.breudes.gerenciaentregas.core.exception.EncomendaValidationException;
import com.breudes.gerenciaentregas.core.repository.EncomendaRepository;

class CriarEncomendaUseCaseTest {

    private static class RepositoryEmMemoria implements EncomendaRepository {
        private Encomenda encomendaSalva;

        @Override
        public Encomenda criarEncomenda(Encomenda encomenda) {
            if (encomenda.getId() == null) {
                encomenda.setId(1L);
            }
            this.encomendaSalva = encomenda;
            return encomenda;
        }

        @Override
        public Encomenda buscarEncomenda(Long idEncomenda) {
            return null;
        }

        @Override
        public Encomenda confirmarEntregaEncomenda(Encomenda encomenda) {
            return encomenda;
        }

        @Override
        public Encomenda enviaNotificacaoEncomenda(Long idEncomenda) {
            if (encomendaSalva == null || !encomendaSalva.getId().equals(idEncomenda)) {
                return null;
            }
            encomendaSalva.setStatus(StatusEncomenda.NOTIFICACAO_ENVIADA);
            return encomendaSalva;
        }

        @Override
        public Encomenda confirmaNotificacaoEncomenda(Long idEncomenda) {
            return null;
        }
    }

    private CriarEncomendaUseCase novoUseCase(RepositoryEmMemoria repository) {
        return new CriarEncomendaUseCase(repository, notificacao -> {});
    }

    @Test
    @DisplayName("Deve criar uma encomenda quando os dados forem válidos.")
    void deveCriarEncomendaQuandoDadosForemValidos() {
        RepositoryEmMemoria repository = new RepositoryEmMemoria();
        List<NotificacaoEmail> notificacoes = new ArrayList<>();
        CriarEncomendaUseCase useCase = new CriarEncomendaUseCase(repository, notificacoes::add);
        Morador morador = new Morador(1L, "Maria Silva", "11999999999", "maria@example.com", "101", "A");
        Encomenda encomenda = new Encomenda(null, "Caixa de ingredientes", morador, null, null, null, true, null);

        Encomenda resultado = useCase.execute(encomenda);

        assertAll(
                () -> assertSame(encomenda, resultado),
            () -> assertSame(encomenda, repository.encomendaSalva),
            () -> assertEquals(StatusEncomenda.NOTIFICACAO_ENVIADA, resultado.getStatus()),
            () -> assertEquals(List.of(new NotificacaoEmail(
                1L, "maria@example.com", "Maria Silva", "Caixa de ingredientes", "101", "A"
            )), notificacoes)
        );
    }

    @Test
    @DisplayName("Deve rejeitar criação quando encomenda for nula.")
    void deveRejeitarCriacaoQuandoEncomendaForNula() {
        RepositoryEmMemoria repository = new RepositoryEmMemoria();
        CriarEncomendaUseCase useCase = novoUseCase(repository);

        EncomendaValidationException erro = assertThrows(
                EncomendaValidationException.class,
                () -> useCase.execute(null)
        );

        assertEquals("Encomenda não pode ser nula.", erro.getMessage());
    }

    @Test
    @DisplayName("Deve rejeitar criação quando descrição for nula.")
    void deveRejeitarCriacaoQuandoDescricaoForNula() {
        RepositoryEmMemoria repository = new RepositoryEmMemoria();
        CriarEncomendaUseCase useCase = novoUseCase(repository);
        Morador morador = new Morador(1L, "Maria Silva", "11999999999", "101", "A");
        Encomenda encomenda = new Encomenda(null, null, morador, null, null, null, true, null);

        EncomendaValidationException erro = assertThrows(
                EncomendaValidationException.class,
                () -> useCase.execute(encomenda)
        );

        assertEquals("Descrição é obrigatória e não pode ser nula.", erro.getMessage());
    }

    @Test
    @DisplayName("Deve rejeitar criação quando descrição for vazia.")
    void deveRejeitarCriacaoQuandoDescricaoForVazia() {
        RepositoryEmMemoria repository = new RepositoryEmMemoria();
        CriarEncomendaUseCase useCase = novoUseCase(repository);
        Morador morador = new Morador(1L, "Maria Silva", "11999999999", "101", "A");
        Encomenda encomenda = new Encomenda(null, "   ", morador, null, null, null, true, null);

        EncomendaValidationException erro = assertThrows(
                EncomendaValidationException.class,
                () -> useCase.execute(encomenda)
        );

        assertEquals("Descrição é obrigatória e não pode ser vazia.", erro.getMessage());
    }

    @Test
    @DisplayName("Deve rejeitar criação quando morador for nulo.")
    void deveRejeitarCriacaoQuandoMoradorForNulo() {
        RepositoryEmMemoria repository = new RepositoryEmMemoria();
        CriarEncomendaUseCase useCase = novoUseCase(repository);
        Encomenda encomenda = new Encomenda(null, "Caixa de ingredientes", null, null, null, null, true, null);

        EncomendaValidationException erro = assertThrows(
                EncomendaValidationException.class,
                () -> useCase.execute(encomenda)
        );

        assertEquals("Morador é obrigatório e não pode ser nulo.", erro.getMessage());
    }

    @Test
    @DisplayName("Deve rejeitar criação quando nome do morador for nulo.")
    void deveRejeitarCriacaoQuandoNomeDoMoradorForNulo() {
        RepositoryEmMemoria repository = new RepositoryEmMemoria();
        CriarEncomendaUseCase useCase = novoUseCase(repository);
        Morador morador = new Morador(1L, null, "11999999999", "101", "A");
        Encomenda encomenda = new Encomenda(null, "Caixa de ingredientes", morador, null, null, null, true, null);

        EncomendaValidationException erro = assertThrows(
                EncomendaValidationException.class,
                () -> useCase.execute(encomenda)
        );

        assertEquals("Nome de morador é obrigatório e não pode ser nulo.", erro.getMessage());
    }

    @Test
    @DisplayName("Deve rejeitar criação quando nome do morador for vazio.")
    void deveRejeitarCriacaoQuandoNomeDoMoradorForVazio() {
        RepositoryEmMemoria repository = new RepositoryEmMemoria();
        CriarEncomendaUseCase useCase = novoUseCase(repository);
        Morador morador = new Morador(1L, "   ", "11999999999", "101", "A");
        Encomenda encomenda = new Encomenda(null, "Caixa de ingredientes", morador, null, null, null, true, null);

        EncomendaValidationException erro = assertThrows(
                EncomendaValidationException.class,
                () -> useCase.execute(encomenda)
        );

        assertEquals("Nome de morador é obrigatório e não pode ser vazio.", erro.getMessage());
    }

    @Test
    @DisplayName("Deve rejeitar criação quando e-mail do morador estiver vazio.")
    void deveRejeitarCriacaoQuandoEmailDoMoradorEstiverVazio() {
        RepositoryEmMemoria repository = new RepositoryEmMemoria();
        CriarEncomendaUseCase useCase = novoUseCase(repository);
        Morador morador = new Morador(1L, "Maria Silva", "11999999999", "   ", "101", "A");
        Encomenda encomenda = new Encomenda(null, "Caixa de ingredientes", morador, null, null, null, true, null);

        EncomendaValidationException erro = assertThrows(
                EncomendaValidationException.class,
                () -> useCase.execute(encomenda)
        );

        assertEquals("E-mail do morador é obrigatório e não pode ser vazio.", erro.getMessage());
    }
}
