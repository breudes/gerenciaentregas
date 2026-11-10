package com.breudes.gerenciaentregas.core.domain;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MoradorTest {

    @Test
    @DisplayName("deve criar morador com todos os dados informados")
    void deveCriarMoradorComTodosOsDadosInformados() {
        Morador morador = new Morador(1L, "Maria Silva", "11999999999", "101", "A");

        assertAll(
                () -> assertEquals(1L, morador.getId()),
                () -> assertEquals("Maria Silva", morador.getNome()),
                () -> assertEquals("11999999999", morador.getTelefone()),
                () -> assertEquals("101", morador.getApartamento()),
                () -> assertEquals("A", morador.getBloco())
        );
    }

    @Test
    @DisplayName("deve atualizar os dados do morador pelos setters")
    void deveAtualizarDadosDoMoradorPelosSetters() {
        Morador morador = new Morador();

        morador.setId(2L);
        morador.setNome("João Pereira");
        morador.setTelefone("11888888888");
        morador.setApartamento("205");
        morador.setBloco("B");

        assertAll(
                () -> assertEquals(2L, morador.getId()),
                () -> assertEquals("João Pereira", morador.getNome()),
                () -> assertEquals("11888888888", morador.getTelefone()),
                () -> assertEquals("205", morador.getApartamento()),
                () -> assertEquals("B", morador.getBloco())
        );
    }

    @Test
    @DisplayName("deve manter o construtor vazio sem valor inicial")
    void deveManterConstrutorVazioSemValoresIniciais() {
        Morador morador = new Morador();

        assertAll(
                () -> assertNull(morador.getId()),
                () -> assertNull(morador.getNome()),
                () -> assertNull(morador.getTelefone()),
                () -> assertNull(morador.getApartamento()),
                () -> assertNull(morador.getBloco())
        );
    }
}
