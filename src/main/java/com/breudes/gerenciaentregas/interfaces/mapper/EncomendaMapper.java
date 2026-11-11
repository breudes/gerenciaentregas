package com.breudes.gerenciaentregas.interfaces.mapper;

import com.breudes.gerenciaentregas.core.domain.Encomenda;
import com.breudes.gerenciaentregas.core.domain.Morador;
import com.breudes.gerenciaentregas.core.domain.StatusEncomenda;
import com.breudes.gerenciaentregas.infrastructures.entity.EncomendaJpa;
import com.breudes.gerenciaentregas.infrastructures.entity.MoradorJpa;
import com.breudes.gerenciaentregas.infrastructures.enums.StatusEncomendaJpa;
import com.breudes.gerenciaentregas.interfaces.dto.encomenda.EncomendaRequestDto;

import java.time.LocalDateTime;

public class EncomendaMapper {
    public static EncomendaJpa converteDomainParaJpa(Encomenda encomenda) {
        if (encomenda == null) {
            return null;
        }

        MoradorJpa moradorDestinatarioJpa = null;
        MoradorJpa moradorRecebedorJpa = null;

        if(encomenda.getMoradorDestinatario() != null)
            moradorDestinatarioJpa = MoradorMapper.converteEntityparaJpa(encomenda.getMoradorDestinatario());
        if(encomenda.getMoradorRecebedor() != null)
            moradorRecebedorJpa = MoradorMapper.converteEntityparaJpa(encomenda.getMoradorRecebedor());

        EncomendaJpa encomendaJpa = new EncomendaJpa();
        encomendaJpa.setId(encomenda.getId());
        encomendaJpa.setDescricao(encomenda.getDescricao());
        encomendaJpa.setMoradorDestinatarioJpa(moradorDestinatarioJpa);
        encomendaJpa.setMoradorRecebedorJpa(moradorRecebedorJpa);
        encomendaJpa.setDataDeCadastro(encomenda.getDataDeCadastro() != null ? encomenda.getDataDeCadastro() : LocalDateTime.now());
        encomendaJpa.setDataDeEntrega(encomenda.getDataDeEntrega());
        encomendaJpa.setAtivo(encomenda.isAtivo());
        encomendaJpa.setStatus(converteStatusDomainParaJpa(encomenda.getStatus()));
        return encomendaJpa;
    }

    public static Encomenda converteJpaParaDomain(EncomendaJpa encomendaJpa) {
        if (encomendaJpa == null) {
            return null;
        }

        Morador moradorDestinatario = null;
        Morador moradorRecebedor = null;

        if(encomendaJpa.getMoradorDestinatarioJpa() != null)
            moradorDestinatario = MoradorMapper.converteJpaParaEntity(encomendaJpa.getMoradorDestinatarioJpa());
        if(encomendaJpa.getMoradorRecebedorJpa() != null)
            moradorRecebedor = MoradorMapper.converteJpaParaEntity(encomendaJpa.getMoradorRecebedorJpa());

        Encomenda encomenda = new Encomenda();
        encomenda.setId(encomendaJpa.getId());
        encomenda.setDescricao(encomendaJpa.getDescricao());
        encomenda.setMoradorDestinatario(moradorDestinatario);
        encomenda.setMoradorRecebedor(moradorRecebedor);
        encomenda.setDataDeCadastro(encomendaJpa.getDataDeCadastro());
        encomenda.setDataDeEntrega(encomendaJpa.getDataDeEntrega());
        encomenda.setAtivo(encomendaJpa.isAtivo());
        encomenda.setStatus(converteStatusJpaParaDomain(encomendaJpa.getStatus()));
        return encomenda;
    }

    public static Encomenda converteRequestDtoParaDomain(EncomendaRequestDto encomendaRequestDto){
        if (encomendaRequestDto == null) {
            return null;
        }

        Morador moradorDestinatario = new Morador(
                null,
                encomendaRequestDto.moradorRequestDTO().nome(),
                encomendaRequestDto.moradorRequestDTO().telefone(),
                encomendaRequestDto.moradorRequestDTO().email(),
                encomendaRequestDto.moradorRequestDTO().apartamento(),
                encomendaRequestDto.moradorRequestDTO().bloco()
        );

        return new Encomenda(
                null,
                encomendaRequestDto.descricao(),
                moradorDestinatario,
                null,
                LocalDateTime.now(),
                null,
                true,
                StatusEncomenda.PENDENTE
        );
    }

    public static StatusEncomenda converteStatusJpaParaDomain(StatusEncomendaJpa status) {
        if (status == null) {
            return null;
        }

        return switch (status) {
            case PENDENTE -> StatusEncomenda.PENDENTE;
            case NOTIFICACAO_ENVIADA -> StatusEncomenda.NOTIFICACAO_ENVIADA;
            case NOTIFICACAO_RECEBIDA -> StatusEncomenda.NOTIFICACAO_RECEBIDA;
            case ENTREGUE -> StatusEncomenda.ENTREGUE;
        };
    }

    public static StatusEncomendaJpa converteStatusDomainParaJpa(StatusEncomenda status) {
        if (status == null) {
            return null;
        }

        return switch (status) {
            case PENDENTE -> StatusEncomendaJpa.PENDENTE;
            case NOTIFICACAO_ENVIADA -> StatusEncomendaJpa.NOTIFICACAO_ENVIADA;
            case NOTIFICACAO_RECEBIDA -> StatusEncomendaJpa.NOTIFICACAO_RECEBIDA;
            case ENTREGUE -> StatusEncomendaJpa.ENTREGUE;
        };
    }
}
