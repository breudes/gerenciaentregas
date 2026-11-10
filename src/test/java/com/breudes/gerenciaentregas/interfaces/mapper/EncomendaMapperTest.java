package com.breudes.gerenciaentregas.interfaces.mapper;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.LocalDateTime;

import com.breudes.gerenciaentregas.core.domain.Encomenda;
import com.breudes.gerenciaentregas.core.domain.Morador;
import com.breudes.gerenciaentregas.core.domain.StatusEncomenda;
import com.breudes.gerenciaentregas.infrastructures.entity.EncomendaJpa;
import com.breudes.gerenciaentregas.infrastructures.entity.MoradorJpa;
import com.breudes.gerenciaentregas.infrastructures.enums.StatusEncomendaJpa;
import com.breudes.gerenciaentregas.interfaces.dto.encomenda.EncomendaRequestDto;
import com.breudes.gerenciaentregas.interfaces.dto.morador.MoradorRequestDTO;
import org.junit.jupiter.api.Test;

class EncomendaMapperTest {

    @Test
    void deveConverterDomainParaJpa() {
        Morador destinatario = new Morador(1L, "Maria", "11999887766", "101", "A");
        Morador recebedor = new Morador(2L, "João", "11988776655", "102", "B");
        Encomenda encomenda = new Encomenda(5L, "Livro de Java", destinatario, recebedor, LocalDateTime.of(2024, 1, 10, 8, 30), null, true, StatusEncomenda.PENDENTE);

        EncomendaJpa resultado = EncomendaMapper.converteDomainParaJpa(encomenda);

        assertAll(
                () -> assertEquals(5L, resultado.getId()),
                () -> assertEquals("Livro de Java", resultado.getDescricao()),
                () -> assertEquals("Maria", resultado.getMoradorDestinatarioJpa().getNome()),
                () -> assertEquals("João", resultado.getMoradorRecebedorJpa().getNome()),
                () -> assertEquals(StatusEncomendaJpa.PENDENTE, resultado.getStatus())
        );
    }

    @Test
    void deveUsarDataAtualQuandoDataDeCadastroNaoInformada() {
        Encomenda encomenda = new Encomenda();
        LocalDateTime antesDaConversao = LocalDateTime.now();

        EncomendaJpa resultado = EncomendaMapper.converteDomainParaJpa(encomenda);

        LocalDateTime depoisDaConversao = LocalDateTime.now();
        assertAll(
                () -> assertNotNull(resultado.getDataDeCadastro()),
                () -> assertFalse(resultado.getDataDeCadastro().isBefore(antesDaConversao)),
                () -> assertFalse(resultado.getDataDeCadastro().isAfter(depoisDaConversao))
        );
    }

    @Test
    void deveConverterJpaParaDomain() {
        EncomendaJpa encomendaJpa = new EncomendaJpa(7L, "Livro de Java",
                new MoradorJpa(1L, "Maria", "11999887766", "101", "A"),
                new MoradorJpa(2L, "João", "11988776655", "102", "B"),
                LocalDateTime.of(2024, 1, 10, 8, 30), null, true, StatusEncomendaJpa.NOTIFICACAO_ENVIADA);

        Encomenda resultado = EncomendaMapper.converteJpaParaDomain(encomendaJpa);

        assertAll(
                () -> assertEquals(7L, resultado.getId()),
                () -> assertEquals("Livro de Java", resultado.getDescricao()),
                () -> assertEquals("Maria", resultado.getMoradorDestinatario().getNome()),
                () -> assertEquals("João", resultado.getMoradorRecebedor().getNome()),
                () -> assertEquals(StatusEncomenda.NOTIFICACAO_ENVIADA, resultado.getStatus())
        );
    }

    @Test
    void deveConverterJpaParaDomainSemMoradores() {
        EncomendaJpa encomendaJpa = new EncomendaJpa(8L, "Encomenda sem moradores",
                null, null, LocalDateTime.of(2024, 1, 10, 8, 30), null, true,
                StatusEncomendaJpa.PENDENTE);

        Encomenda resultado = EncomendaMapper.converteJpaParaDomain(encomendaJpa);

        assertAll(
                () -> assertNull(resultado.getMoradorDestinatario()),
                () -> assertNull(resultado.getMoradorRecebedor())
        );
    }

    @Test
    void deveConverterRequestDtoParaDomain() {
        MoradorRequestDTO moradorRequest = new MoradorRequestDTO("Maria", "11999887766", "101", "A");
        EncomendaRequestDto dto = new EncomendaRequestDto("Livro de Java", moradorRequest);

        Encomenda resultado = EncomendaMapper.converteRequestDtoParaDomain(dto);

        assertAll(
                () -> assertEquals("Livro de Java", resultado.getDescricao()),
                () -> assertEquals("Maria", resultado.getMoradorDestinatario().getNome()),
                () -> assertNotNull(resultado.getDataDeCadastro()),
                () -> assertEquals(StatusEncomenda.PENDENTE, resultado.getStatus())
        );
    }

    @Test
    void deveConverterStatusJpaParaDomain() {
        assertAll(
                () -> assertEquals(StatusEncomenda.PENDENTE, EncomendaMapper.converteStatusJpaParaDomain(StatusEncomendaJpa.PENDENTE)),
                () -> assertEquals(StatusEncomenda.NOTIFICACAO_ENVIADA, EncomendaMapper.converteStatusJpaParaDomain(StatusEncomendaJpa.NOTIFICACAO_ENVIADA)),
                () -> assertEquals(StatusEncomenda.NOTIFICACAO_RECEBIDA, EncomendaMapper.converteStatusJpaParaDomain(StatusEncomendaJpa.NOTIFICACAO_RECEBIDA)),
                () -> assertEquals(StatusEncomenda.ENTREGUE, EncomendaMapper.converteStatusJpaParaDomain(StatusEncomendaJpa.ENTREGUE))
        );
    }

    @Test
    void deveConverterStatusDomainParaJpa() {
        assertAll(
                () -> assertEquals(StatusEncomendaJpa.PENDENTE, EncomendaMapper.converteStatusDomainParaJpa(StatusEncomenda.PENDENTE)),
                () -> assertEquals(StatusEncomendaJpa.NOTIFICACAO_ENVIADA, EncomendaMapper.converteStatusDomainParaJpa(StatusEncomenda.NOTIFICACAO_ENVIADA)),
                () -> assertEquals(StatusEncomendaJpa.NOTIFICACAO_RECEBIDA, EncomendaMapper.converteStatusDomainParaJpa(StatusEncomenda.NOTIFICACAO_RECEBIDA)),
                () -> assertEquals(StatusEncomendaJpa.ENTREGUE, EncomendaMapper.converteStatusDomainParaJpa(StatusEncomenda.ENTREGUE))
        );
    }

    @Test
    void deveRetornarNullQuandoParametroForNulo() {
        assertAll(
                () -> assertNull(EncomendaMapper.converteDomainParaJpa(null)),
                () -> assertNull(EncomendaMapper.converteJpaParaDomain(null)),
                () -> assertNull(EncomendaMapper.converteRequestDtoParaDomain(null)),
                () -> assertNull(EncomendaMapper.converteStatusJpaParaDomain(null)),
                () -> assertNull(EncomendaMapper.converteStatusDomainParaJpa(null))
        );
    }
}
