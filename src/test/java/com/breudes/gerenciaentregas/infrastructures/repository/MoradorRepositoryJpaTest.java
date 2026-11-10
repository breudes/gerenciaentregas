package com.breudes.gerenciaentregas.infrastructures.repository;

import com.breudes.gerenciaentregas.core.domain.Morador;
import com.breudes.gerenciaentregas.infrastructures.entity.MoradorJpa;
import com.breudes.gerenciaentregas.infrastructures.springdatajpa.SpringDataJpaMorador;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class MoradorRepositoryJpaTest {

    private final SpringDataJpaMorador springDataJpaMorador = mock(SpringDataJpaMorador.class);
    private final MoradorRepositoryJpa repository = new MoradorRepositoryJpa(springDataJpaMorador);

    @Test
    void deveCriarMoradorQuandoTelefoneNaoEstiverRegistrado() {
        Morador morador = new Morador(null, "Maria Silva", "11999999999", "101", "A");
        when(springDataJpaMorador.findByTelefone("11999999999")).thenReturn(Optional.empty());
        when(springDataJpaMorador.save(any(MoradorJpa.class))).thenAnswer(invocation -> {
            MoradorJpa moradorJpa = invocation.getArgument(0);
            moradorJpa.setId(1L);
            return moradorJpa;
        });

        Morador resultado = repository.criarMorador(morador);

        assertAll(
                () -> assertEquals(1L, resultado.getId()),
                () -> assertEquals("Maria Silva", resultado.getNome()),
                () -> assertEquals("11999999999", resultado.getTelefone()),
                () -> assertEquals("101", resultado.getApartamento()),
                () -> assertEquals("A", resultado.getBloco())
        );

        ArgumentCaptor<MoradorJpa> captor = ArgumentCaptor.forClass(MoradorJpa.class);
        verify(springDataJpaMorador).save(captor.capture());
        assertEquals("Maria Silva", captor.getValue().getNome());
    }

    @Test
    void deveLancarExcecaoAoCriarMoradorComTelefoneJaRegistrado() {
        Morador morador = new Morador(null, "Maria Silva", "11999999999", "101", "A");
        when(springDataJpaMorador.findByTelefone("11999999999"))
                .thenReturn(Optional.of(new MoradorJpa(2L, "José", "11999999999", "202", "B")));

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> repository.criarMorador(morador)
        );

        assertEquals("Telefone já registrado.", exception.getMessage());
    }

    @Test
    void deveBuscarMoradorPorTelefone() {
        MoradorJpa moradorJpa = new MoradorJpa(3L, "Carlos", "11988888888", "303", "C");
        when(springDataJpaMorador.findByTelefone("11988888888")).thenReturn(Optional.of(moradorJpa));

        Optional<MoradorJpa> resultado = repository.buscarMorador("11988888888");

        assertTrue(resultado.isPresent());
        assertEquals("Carlos", resultado.get().getNome());
    }

    @Test
    void deveRetornarOpcionalVazioQuandoTelefoneEhNulo() {
        assertTrue(repository.buscarMorador(null).isEmpty());
    }

    @Test
    void deveAtualizarMoradorQuandoTelefoneAtualEhIgualAoNovoTelefone() {
        Morador moradorExistente = new Morador(5L, "Maria Atualizada", "11999999999", "404", "D");
        MoradorJpa moradorJpa = new MoradorJpa(5L, "Maria Antiga", "11999999999", "101", "A");

        when(springDataJpaMorador.findByTelefone("11999999999")).thenReturn(Optional.of(moradorJpa));
        when(springDataJpaMorador.save(any(MoradorJpa.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Morador resultado = repository.atualizarMorador("11999999999", moradorExistente);

        assertAll(
                () -> assertEquals(5L, resultado.getId()),
                () -> assertEquals("Maria Atualizada", resultado.getNome()),
                () -> assertEquals("11999999999", resultado.getTelefone()),
                () -> assertEquals("404", resultado.getApartamento()),
                () -> assertEquals("D", resultado.getBloco())
        );
    }

    @Test
    void deveAtualizarMoradorQuandoTelefoneAtualNaoContemNovoTelefone() {
        Morador moradorExistente = new Morador(5L, "Maria Atualizada", "11988888888", "404", "D");
        MoradorJpa moradorJpa = new MoradorJpa(5L, "Maria Antiga", "11999999999", "101", "A");

        when(springDataJpaMorador.findByTelefone("11999999999")).thenReturn(Optional.of(moradorJpa));
        when(springDataJpaMorador.findByTelefone("11988888888")).thenReturn(Optional.empty());
        when(springDataJpaMorador.save(any(MoradorJpa.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Morador resultado = repository.atualizarMorador("11999999999", moradorExistente);

        assertAll(
                () -> assertEquals(5L, resultado.getId()),
                () -> assertEquals("Maria Atualizada", resultado.getNome()),
                () -> assertEquals("11988888888", resultado.getTelefone()),
                () -> assertEquals("404", resultado.getApartamento()),
                () -> assertEquals("D", resultado.getBloco())
        );
    }

    @Test
    void devePermitirAtualizarMoradorQuandoTelefonePertenceAoMesmoMorador() {
        Morador moradorExistente = new Morador(5L, "Maria Atualizada", "11988888888", "404", "D");
        MoradorJpa moradorJpa = new MoradorJpa(5L, "Maria Antiga", "11999999999", "101", "A");

        when(springDataJpaMorador.findByTelefone("11999999999")).thenReturn(Optional.of(moradorJpa));
        when(springDataJpaMorador.findByTelefone("11988888888")).thenReturn(Optional.of(moradorJpa));
        when(springDataJpaMorador.save(any(MoradorJpa.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Morador resultado = assertDoesNotThrow(() -> repository.atualizarMorador("11999999999", moradorExistente));

        assertAll(
                () -> assertEquals(5L, resultado.getId()),
                () -> assertEquals("Maria Atualizada", resultado.getNome()),
                () -> assertEquals("11988888888", resultado.getTelefone()),
                () -> assertEquals("404", resultado.getApartamento()),
                () -> assertEquals("D", resultado.getBloco())
        );
    }

    @Test
    void deveLancarExcecaoAoAtualizarMoradorQuandoNovoTelefoneJaEstiverRegistradoEmOutroMorador() {
        Morador moradorExistente = new Morador(5L, "Maria Atualizada", "11988888888", "404", "D");
        MoradorJpa moradorJpa = new MoradorJpa(5L, "Maria Antiga", "11999999999", "101", "A");
        MoradorJpa outroMorador = new MoradorJpa(9L, "João", "11988888888", "909", "Z");

        when(springDataJpaMorador.findByTelefone("11999999999")).thenReturn(Optional.of(moradorJpa));
        when(springDataJpaMorador.findByTelefone("11988888888")).thenReturn(Optional.of(outroMorador));

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> repository.atualizarMorador("11999999999", moradorExistente)
        );

        assertEquals("Telefone que deseja inserir já está registrado em nosso sistema.", exception.getMessage());
    }

    @Test
    void deveRetornarNullQuandoMoradorExistenteForNulo() {
        assertNull(repository.atualizarMorador("11999999999", null));
    }

    @Test
    void deveRetornarNullQuandoTelefoneAtualForNulo() {
        Morador moradorExistente = new Morador(5L, "Maria Atualizada", "11999999999", "404", "D");

        assertNull(repository.atualizarMorador(null, moradorExistente));
    }

    @Test
    void deveRetornarNullQuandoTelefoneAtualEstiverEmBranco() {
        Morador moradorExistente = new Morador(5L, "Maria Atualizada", "11999999999", "404", "D");

        assertNull(repository.atualizarMorador("   ", moradorExistente));
    }

    @Test
    void deveLancarExcecaoAoAtualizarMoradorQuandoTelefoneAtualNaoExiste() {
        when(springDataJpaMorador.findByTelefone("11999999999")).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> repository.atualizarMorador("11999999999", new Morador(7L, "Ana", "11999999999", "707", "G"))
        );

        assertEquals("Morador não foi encontrado com este telefone atual.", exception.getMessage());
    }
}
