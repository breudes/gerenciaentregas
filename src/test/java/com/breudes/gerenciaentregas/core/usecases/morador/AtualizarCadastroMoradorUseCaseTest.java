package com.breudes.gerenciaentregas.core.usecases.morador;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.breudes.gerenciaentregas.core.domain.Morador;
import com.breudes.gerenciaentregas.core.exception.MoradorValidationException;
import com.breudes.gerenciaentregas.core.repository.MoradorRepository;

class AtualizarCadastroMoradorUseCaseTest {

    private static class RepositoryEmMemoria implements MoradorRepository {
        private final Morador moradorExistente;
        private final Set<String> telefonesRegistrados = new HashSet<>();
        private Morador moradorAtualizado;

        private RepositoryEmMemoria(Morador moradorExistente) {
            this.moradorExistente = moradorExistente;
            this.telefonesRegistrados.add(moradorExistente.getTelefone());
        }

        @Override
        public Morador criarMorador(Morador novoMorador) {
            return novoMorador;
        }

        @Override
        public Morador buscarMorador(Long idMorador) {
            return idMorador.equals(moradorExistente.getId()) ? moradorExistente : null;
        }

        @Override
        public Morador atualizarMorador(Morador moradorExistente) {
            if (moradorExistente.getTelefone() != null && !moradorExistente.getTelefone().equals(this.moradorExistente.getTelefone())
                    && telefonesRegistrados.contains(moradorExistente.getTelefone())) {
                throw new IllegalArgumentException("Telefone já registrado.");
            }

            this.moradorAtualizado = moradorExistente;
            telefonesRegistrados.remove(this.moradorExistente.getTelefone());
            telefonesRegistrados.add(moradorExistente.getTelefone());
            return moradorExistente;
        }
    }

    @Test
    @DisplayName("Deve rejeitar atualização quando morador for nulo")
    void deveRejeitarAtualizacaoQuandoMoradorForNulo() {
        Morador moradorExistente = new Morador(1L, "Maria Silva", "11999999999", "101", "A");
        RepositoryEmMemoria repository = new RepositoryEmMemoria(moradorExistente);
        AtualizarMoradorUseCase useCase = new AtualizarMoradorUseCase(repository);

        MoradorValidationException erro = assertThrows(
                MoradorValidationException.class,
                () -> useCase.execute(null)
        );

        assertEquals("Morador não pode ser nulo.", erro.getMessage());
    }

    @Test
    @DisplayName("Deve atualizar um morador quando os dados são válidos")
    void deveAtualizarMoradorQuandoDadosSaoValidos() {
        Morador moradorExistente = new Morador(1L, "Maria Silva", "11999999999", "101", "A");
        RepositoryEmMemoria repository = new RepositoryEmMemoria(moradorExistente);
        AtualizarMoradorUseCase useCase = new AtualizarMoradorUseCase(repository);
        Morador moradorAtualizado = new Morador(1L, "Maria Souza", "11888888888", "202", "B");

        Morador resultado = useCase.execute(moradorAtualizado);

        assertAll(
                () -> assertSame(moradorAtualizado, resultado),
                () -> assertSame(moradorAtualizado, repository.moradorAtualizado)
        );
    }

    @Test
    @DisplayName("Deve rejeitar atualização quando morador não existir")
    void deveRejeitarAtualizacaoQuandoMoradorNaoExistir() {
        RepositoryEmMemoria repository = new RepositoryEmMemoria(new Morador(1L, "Maria Silva", "11999999999", "101", "A"));
        AtualizarMoradorUseCase useCase = new AtualizarMoradorUseCase(repository);

        MoradorValidationException erro = assertThrows(
                MoradorValidationException.class,
                () -> useCase.execute(new Morador(2L, "Maria Souza", "11888888888", "202", "B"))
        );

        assertEquals("Morador não encontrado no sistema.", erro.getMessage());
    }

    @Test
    @DisplayName("Deve rejeitar atualização quando telefone já estiver sendo usado por outro morador")
    void deveRejeitarAtualizacaoQuandoTelefoneJaEstiverSendoUsadoPorOutroMorador() {
        Morador moradorExistente = new Morador(1L, "Maria Silva", "11999999999", "101", "A");
        RepositoryEmMemoria repository = new RepositoryEmMemoria(moradorExistente);
        repository.telefonesRegistrados.add("11888888888");
        AtualizarMoradorUseCase useCase = new AtualizarMoradorUseCase(repository);

        IllegalArgumentException erro = assertThrows(
                IllegalArgumentException.class,
                () -> useCase.execute(new Morador(1L, "Maria Souza", "11888888888", "202", "B"))
        );

        assertEquals("Telefone já registrado.", erro.getMessage());
    }

    @Test
    @DisplayName("Deve rejeitar atualização quando telefone for nulo")
    void deveRejeitarAtualizacaoQuandoTelefoneForNulo() {
        Morador moradorExistente = new Morador(1L, "Maria Silva", "11999999999", "101", "A");
        RepositoryEmMemoria repository = new RepositoryEmMemoria(moradorExistente);
        AtualizarMoradorUseCase useCase = new AtualizarMoradorUseCase(repository);

        MoradorValidationException erro = assertThrows(
                MoradorValidationException.class,
                () -> useCase.execute(new Morador(1L, "Maria Souza", null, "202", "B"))
        );

        assertEquals("Telefone do morador é obrigatório.", erro.getMessage());
    }

    @Test
    @DisplayName("Deve rejeitar atualização quando telefone for vazio")
    void deveRejeitarAtualizacaoQuandoTelefoneForVazio() {
        Morador moradorExistente = new Morador(1L, "Maria Silva", "11999999999", "101", "A");
        RepositoryEmMemoria repository = new RepositoryEmMemoria(moradorExistente);
        AtualizarMoradorUseCase useCase = new AtualizarMoradorUseCase(repository);

        MoradorValidationException erro = assertThrows(
                MoradorValidationException.class,
                () -> useCase.execute(new Morador(1L, "Maria Souza", "   ", "202", "B"))
        );

        assertEquals("Telefone do morador é obrigatório.", erro.getMessage());
    }

    @Test
    @DisplayName("Deve rejeitar atualização quando apartamento for nulo")
    void deveRejeitarAtualizacaoQuandoApartamentoForNulo() {
        Morador moradorExistente = new Morador(1L, "Maria Silva", "11999999999", "101", "A");
        RepositoryEmMemoria repository = new RepositoryEmMemoria(moradorExistente);
        AtualizarMoradorUseCase useCase = new AtualizarMoradorUseCase(repository);

        MoradorValidationException erro = assertThrows(
                MoradorValidationException.class,
                () -> useCase.execute(new Morador(1L, "Maria Souza", "11888888888", null, "B"))
        );

        assertEquals("Apartamento do morador é obrigatório.", erro.getMessage());
    }

    @Test
    @DisplayName("Deve rejeitar atualização quando apartamento for vazio")
    void deveRejeitarAtualizacaoQuandoApartamentoForVazio() {
        Morador moradorExistente = new Morador(1L, "Maria Silva", "11999999999", "101", "A");
        RepositoryEmMemoria repository = new RepositoryEmMemoria(moradorExistente);
        AtualizarMoradorUseCase useCase = new AtualizarMoradorUseCase(repository);

        MoradorValidationException erro = assertThrows(
                MoradorValidationException.class,
                () -> useCase.execute(new Morador(1L, "Maria Souza", "11888888888", "   ", "B"))
        );

        assertEquals("Apartamento do morador é obrigatório.", erro.getMessage());
    }

    @Test
    @DisplayName("Deve rejeitar atualização quando bloco for nulo")
    void deveRejeitarAtualizacaoQuandoBlocoForNulo() {
        Morador moradorExistente = new Morador(1L, "Maria Silva", "11999999999", "101", "A");
        RepositoryEmMemoria repository = new RepositoryEmMemoria(moradorExistente);
        AtualizarMoradorUseCase useCase = new AtualizarMoradorUseCase(repository);

        MoradorValidationException erro = assertThrows(
                MoradorValidationException.class,
                () -> useCase.execute(new Morador(1L, "Maria Souza", "11888888888", "202", null))
        );

        assertEquals("Bloco do morador é obrigatório.", erro.getMessage());
    }

    @Test
    @DisplayName("Deve rejeitar atualização quando bloco for vazio")
    void deveRejeitarAtualizacaoQuandoBlocoForVazio() {
        Morador moradorExistente = new Morador(1L, "Maria Silva", "11999999999", "101", "A");
        RepositoryEmMemoria repository = new RepositoryEmMemoria(moradorExistente);
        AtualizarMoradorUseCase useCase = new AtualizarMoradorUseCase(repository);

        MoradorValidationException erro = assertThrows(
                MoradorValidationException.class,
                () -> useCase.execute(new Morador(1L, "Maria Souza", "11888888888", "202", "   "))
        );

        assertEquals("Bloco do morador é obrigatório.", erro.getMessage());
    }
}
