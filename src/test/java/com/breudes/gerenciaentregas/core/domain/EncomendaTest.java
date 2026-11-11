package com.breudes.gerenciaentregas.core.domain;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class EncomendaTest {

    @Test
    @DisplayName("deve criar encomenda com todos os dados informados")
    void deveCriarEncomendaComTodosOsDadosInformados() {
        Morador destinatario = new Morador(1L, "Maria Silva", "11999999999", "101", "A");
        Morador recebedor = new Morador(2L, "João Pereira", "11888888888", "205", "B");
        LocalDateTime dataCadastro = LocalDateTime.of(2026, 10, 29, 18, 0);
        LocalDateTime dataEntrega = LocalDateTime.of(2026, 10, 30, 9, 30);

        Encomenda encomenda = new Encomenda(10L, "Caixa de livros", destinatario, recebedor, dataCadastro, dataEntrega, true, StatusEncomenda.ENTREGUE);

        assertAll(
                () -> assertEquals(10L, encomenda.getId()),
                () -> assertEquals("Caixa de livros", encomenda.getDescricao()),
                () -> assertEquals(destinatario, encomenda.getMoradorDestinatario()),
                () -> assertEquals(recebedor, encomenda.getMoradorRecebedor()),
                () -> assertEquals(dataCadastro, encomenda.getDataDeCadastro()),
                () -> assertEquals(dataEntrega, encomenda.getDataDeEntrega()),
                () -> assertTrue(encomenda.isAtivo()),
                () -> assertEquals(StatusEncomenda.ENTREGUE, encomenda.getStatus())
        );
    }

    @Test
    @DisplayName("deve atualizar os dados da encomenda pelos setters")
    void deveAtualizarDadosDaEncomendaPelosSetters() {
        Encomenda encomenda = new Encomenda();
        Morador destinatario = new Morador(3L, "Ana Souza", "11777777777", "303", "C");
        Morador recebedor = new Morador(4L, "Carlos Lima", "11666666666", "404", "D");
        LocalDateTime dataCadastro = LocalDateTime.of(2026, 11, 1, 10, 15);
        LocalDateTime dataEntrega = LocalDateTime.of(2026, 11, 2, 15, 45);

        encomenda.setId(11L);
        encomenda.setDescricao("Kit de manutenção");
        encomenda.setMoradorDestinatario(destinatario);
        encomenda.setMoradorRecebedor(recebedor);
        encomenda.setDataDeCadastro(dataCadastro);
        encomenda.setDataDeEntrega(dataEntrega);
        encomenda.setAtivo(false);
        encomenda.setStatus(StatusEncomenda.NOTIFICACAO_ENVIADA);

        assertAll(
                () -> assertEquals(11L, encomenda.getId()),
                () -> assertEquals("Kit de manutenção", encomenda.getDescricao()),
                () -> assertEquals(destinatario, encomenda.getMoradorDestinatario()),
                () -> assertEquals(recebedor, encomenda.getMoradorRecebedor()),
                () -> assertEquals(dataCadastro, encomenda.getDataDeCadastro()),
                () -> assertEquals(dataEntrega, encomenda.getDataDeEntrega()),
                () -> assertFalse(encomenda.isAtivo()),
                () -> assertEquals(StatusEncomenda.NOTIFICACAO_ENVIADA, encomenda.getStatus())
        );
    }

    @Test
    @DisplayName("deve manter o construtor vazio sem valor inicial")
    void deveManterConstrutorVazioSemValoresIniciais() {
        Encomenda encomenda = new Encomenda();

        assertAll(
                () -> assertNull(encomenda.getId()),
                () -> assertNull(encomenda.getDescricao()),
                () -> assertNull(encomenda.getMoradorDestinatario()),
                () -> assertNull(encomenda.getMoradorRecebedor()),
                () -> assertNull(encomenda.getDataDeCadastro()),
                () -> assertNull(encomenda.getDataDeEntrega()),
                () -> assertFalse(encomenda.isAtivo()),
                () -> assertNull(encomenda.getStatus())
        );
    }
}
