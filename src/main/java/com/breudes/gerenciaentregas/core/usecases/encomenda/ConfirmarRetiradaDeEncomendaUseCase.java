package com.breudes.gerenciaentregas.core.usecases.encomenda;

import com.breudes.gerenciaentregas.core.domain.Encomenda;
import com.breudes.gerenciaentregas.core.domain.StatusEncomenda;
import com.breudes.gerenciaentregas.core.exception.EncomendaValidationException;
import com.breudes.gerenciaentregas.core.repository.EncomendaRepository;

public class ConfirmarRetiradaDeEncomendaUseCase {
    private final EncomendaRepository encomendaRepository;

    public ConfirmarRetiradaDeEncomendaUseCase(EncomendaRepository encomendaRepository) {
        this.encomendaRepository = encomendaRepository;
    }

    public Encomenda execute(Encomenda encomenda){
        validaEncomenda(encomenda);
        return encomendaRepository.confirmarEntregaEncomenda(encomenda);
    }

    public void validaEncomenda(Encomenda encomenda){
        if (encomenda == null) {
            throw new EncomendaValidationException("encomenda", "Encomenda não pode ser nula.");
        }

        Encomenda encomendaExistente = encomendaRepository.buscarEncomenda(encomenda.getId());
        if (encomendaExistente == null) {
            throw new EncomendaValidationException("encomenda", "Encomenda não encontrada no sistema.");
        }

        if (encomendaExistente.getStatus() == null) {
            throw new EncomendaValidationException("status",
                    "Status da encomenda não pode ser nulo.");
        }

        if (encomendaExistente.getStatus() != StatusEncomenda.NOTIFICACAO_ENVIADA
                && encomendaExistente.getStatus() != StatusEncomenda.NOTIFICACAO_RECEBIDA) {
            throw new EncomendaValidationException("status",
                    "Encomenda ainda não foi notificada ao destinatário, não é possível confirmar a retirada.");
        }
    }
}
