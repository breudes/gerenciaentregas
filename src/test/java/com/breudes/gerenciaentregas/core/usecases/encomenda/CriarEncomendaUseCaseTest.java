package com.breudes.gerenciaentregas.core.usecases.encomenda;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.breudes.gerenciaentregas.core.domain.Encomenda;
import com.breudes.gerenciaentregas.core.domain.Morador;
import com.breudes.gerenciaentregas.core.exception.EncomendaValidationException;
import com.breudes.gerenciaentregas.core.repository.EncomendaRepository;

class CriarEncomendaUseCaseTest {

    private static class RepositoryEmMemoria implements EncomendaRepository {
        private Encomenda encomendaSalva;

        @Override
        public Encomenda criarEncomenda(Encomenda encomenda) {
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
        public boolean confirmaNotificacaoEncomenda(Long idEncomenda) {
            return false;
        }
    }

    @Test
    @DisplayName("Deve criar uma encomenda quando os dados forem válidos.")
    void deveCriarEncomendaQuandoDadosForemValidos() {
        RepositoryEmMemoria repository = new RepositoryEmMemoria();
        CriarEncomendaUseCase useCase = new CriarEncomendaUseCase(repository);
        Morador morador = new Morador(1L, "Maria Silva", "11999999999", "101", "A");
        Encomenda encomenda = new Encomenda(null, "Caixa de ingredientes", morador, null, null, null, true, null);

        Encomenda resultado = useCase.execute(encomenda);

        assertAll(
                () -> assertSame(encomenda, resultado),
                () -> assertSame(encomenda, repository.encomendaSalva)
        );
    }

    @Test
    @DisplayName("Deve rejeitar criação quando encomenda for nula.")
    void deveRejeitarCriacaoQuandoEncomendaForNula() {
        RepositoryEmMemoria repository = new RepositoryEmMemoria();
        CriarEncomendaUseCase useCase = new CriarEncomendaUseCase(repository);

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
        CriarEncomendaUseCase useCase = new CriarEncomendaUseCase(repository);
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
        CriarEncomendaUseCase useCase = new CriarEncomendaUseCase(repository);
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
        CriarEncomendaUseCase useCase = new CriarEncomendaUseCase(repository);
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
        CriarEncomendaUseCase useCase = new CriarEncomendaUseCase(repository);
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
        CriarEncomendaUseCase useCase = new CriarEncomendaUseCase(repository);
        Morador morador = new Morador(1L, "   ", "11999999999", "101", "A");
        Encomenda encomenda = new Encomenda(null, "Caixa de ingredientes", morador, null, null, null, true, null);

        EncomendaValidationException erro = assertThrows(
                EncomendaValidationException.class,
                () -> useCase.execute(encomenda)
        );

        assertEquals("Nome de morador é obrigatório e não pode ser vazio.", erro.getMessage());
    }
}
