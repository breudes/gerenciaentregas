package com.breudes.gerenciaentregas.core.usecases.encomenda;

import com.breudes.gerenciaentregas.core.domain.Encomenda;
import com.breudes.gerenciaentregas.core.domain.StatusEncomenda;
import com.breudes.gerenciaentregas.core.exception.EncomendaValidationException;
import com.breudes.gerenciaentregas.core.repository.EncomendaRepository;

public class ConfirmarNotificacaoDeEncomendaUseCase {
    private final EncomendaRepository encomendaRepository;

    public ConfirmarNotificacaoDeEncomendaUseCase(EncomendaRepository encomendaRepository) {
        this.encomendaRepository = encomendaRepository;
    }

    public Encomenda execute(Long idEncomenda){
        validaEncomenda(idEncomenda);
        return encomendaRepository.confirmaNotificacaoEncomenda(idEncomenda);
    }

    public void validaEncomenda(Long idEncomenda){
        if (idEncomenda == null) {
            throw new EncomendaValidationException("id", "ID de encomenda não pode ser nulo.");
        }

        Encomenda encomendaExistente = encomendaRepository.buscarEncomenda(idEncomenda);
        if (encomendaExistente == null) {
            throw new EncomendaValidationException("encomenda", "Encomenda não encontrada no sistema.");
        }

        if (encomendaExistente.getStatus() == null) {
            throw new EncomendaValidationException("status",
                    "Status da encomenda não pode ser nulo.");
        }

        if (encomendaExistente.getStatus() != StatusEncomenda.NOTIFICACAO_ENVIADA) {
            throw new EncomendaValidationException("status",
                    "Notificação ainda não foi enviada.");
        }
    }
}
