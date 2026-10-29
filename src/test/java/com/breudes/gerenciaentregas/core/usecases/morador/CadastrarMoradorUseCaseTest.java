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

class CadastrarMoradorUseCaseTest {

    private static class RepositoryEmMemoria implements MoradorRepository {
        private Morador moradorSalvo;
        private final Set<String> telefonesRegistrados = new HashSet<>();

        @Override
        public Morador criarMorador(Morador novoMorador) {
            if (novoMorador.getTelefone() != null && telefonesRegistrados.contains(novoMorador.getTelefone())) {
                throw new IllegalArgumentException("Telefone já registrado.");
            }

            this.moradorSalvo = novoMorador;
            telefonesRegistrados.add(novoMorador.getTelefone());
            return novoMorador;
        }

        @Override
        public Morador buscarMorador(Long idMorador) {
            return null;
        }

        @Override
        public Morador atualizarMorador(Morador moradorExistente) {
            return moradorExistente;
        }
    }

    @Test
    @DisplayName("Deve cadastrar um morador quando os dados são válidos.")
    void deveCadastrarMoradorQuandoDadosSaoValidos() {
        RepositoryEmMemoria repository = new RepositoryEmMemoria();
        CriarMoradorUseCase useCase = new CriarMoradorUseCase(repository);
        Morador morador = new Morador(null, "Maria Silva", "11999999999", "101", "A");

        Morador resultado = useCase.execute(morador);

        assertAll(
                () -> assertSame(morador, resultado),
                () -> assertSame(morador, repository.moradorSalvo)
        );
    }

    @Test
    @DisplayName("Deve rejeitar cadastro quando telefone já estiver sendo usado por outro morador.")
    void deveRejeitarCadastroQuandoTelefoneJaEstiverSendoUsadoPorOutroMorador() {
        RepositoryEmMemoria repository = new RepositoryEmMemoria();
        repository.telefonesRegistrados.add("11999999999");
        repository.moradorSalvo = new Morador(1L, "José Silva", "11999999999", "202", "B");

        CriarMoradorUseCase useCase = new CriarMoradorUseCase(repository);

        IllegalArgumentException erro = assertThrows(
                IllegalArgumentException.class,
                () -> useCase.execute(new Morador(null, "Maria Silva", "11999999999", "101", "A"))
        );

        assertEquals("Telefone já registrado.", erro.getMessage());
    }

    @Test
    @DisplayName("Deve rejeitar cadastro quando morador for nulo.")
    void deveRejeitarCadastroQuandoMoradorForNulo() {
        RepositoryEmMemoria repository = new RepositoryEmMemoria();
        CriarMoradorUseCase useCase = new CriarMoradorUseCase(repository);

        MoradorValidationException erro = assertThrows(
                MoradorValidationException.class,
                () -> useCase.execute(null)
        );

        assertEquals("Morador não pode ser nulo.", erro.getMessage());
    }

    @Test
    @DisplayName("Deve rejeitar cadastro quando telefone for nulo.")
    void deveRejeitarCadastroQuandoTelefoneForNulo() {
        RepositoryEmMemoria repository = new RepositoryEmMemoria();
        CriarMoradorUseCase useCase = new CriarMoradorUseCase(repository);

        MoradorValidationException erro = assertThrows(
                MoradorValidationException.class,
                () -> useCase.execute(new Morador(null, "Maria Silva", null, "101", "A"))
        );

        assertEquals("Telefone do morador é obrigatório.", erro.getMessage());
    }

    @Test
    @DisplayName("Deve rejeitar cadastro quando telefone for vazio.")
    void deveRejeitarCadastroQuandoTelefoneForVazio() {
        RepositoryEmMemoria repository = new RepositoryEmMemoria();
        CriarMoradorUseCase useCase = new CriarMoradorUseCase(repository);

        MoradorValidationException erro = assertThrows(
                MoradorValidationException.class,
                () -> useCase.execute(new Morador(null, "Maria Silva", "   ", "101", "A"))
        );

        assertEquals("Telefone do morador é obrigatório.", erro.getMessage());
    }

    @Test
    @DisplayName("Deve rejeitar cadastro quando apartamento for nulo.")
    void deveRejeitarCadastroQuandoApartamentoForNulo() {
        RepositoryEmMemoria repository = new RepositoryEmMemoria();
        CriarMoradorUseCase useCase = new CriarMoradorUseCase(repository);

        MoradorValidationException erro = assertThrows(
                MoradorValidationException.class,
                () -> useCase.execute(new Morador(null, "Maria Silva", "11999999999", null, "A"))
        );

        assertEquals("Apartamento do morador é obrigatório.", erro.getMessage());
    }

    @Test
    @DisplayName("Deve rejeitar cadastro quando apartamento for vazio.")
    void deveRejeitarCadastroQuandoApartamentoForVazio() {
        RepositoryEmMemoria repository = new RepositoryEmMemoria();
        CriarMoradorUseCase useCase = new CriarMoradorUseCase(repository);

        MoradorValidationException erro = assertThrows(
                MoradorValidationException.class,
                () -> useCase.execute(new Morador(null, "Maria Silva", "11999999999", "    ", "A"))
        );

        assertEquals("Apartamento do morador é obrigatório.", erro.getMessage());
    }

    @Test
    @DisplayName("Deve rejeitar cadastro quando bloco for nulo.")
    void deveRejeitarCadastroQuandoBlocoForNulo() {
        RepositoryEmMemoria repository = new RepositoryEmMemoria();
        CriarMoradorUseCase useCase = new CriarMoradorUseCase(repository);

        MoradorValidationException erro = assertThrows(
                MoradorValidationException.class,
                () -> useCase.execute(new Morador(null, "Maria Silva", "11999999999", "101", null))
        );

        assertEquals("Bloco do morador é obrigatório.", erro.getMessage());
    }

    @Test
    @DisplayName("Deve rejeitar cadastro quando bloco for vazio.")
    void deveRejeitarCadastroQuandoBlocoForVazio() {
        RepositoryEmMemoria repository = new RepositoryEmMemoria();
        CriarMoradorUseCase useCase = new CriarMoradorUseCase(repository);

        MoradorValidationException erro = assertThrows(
                MoradorValidationException.class,
                () -> useCase.execute(new Morador(null, "Maria Silva", "11999999999", "101", "    "))
        );

        assertEquals("Bloco do morador é obrigatório.", erro.getMessage());
    }
}
