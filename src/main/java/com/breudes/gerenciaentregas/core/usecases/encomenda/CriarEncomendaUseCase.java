package com.breudes.gerenciaentregas.core.usecases.encomenda;

import com.breudes.gerenciaentregas.core.domain.Encomenda;
import com.breudes.gerenciaentregas.core.domain.Morador;
import com.breudes.gerenciaentregas.core.event.NotificacaoEmail;
import com.breudes.gerenciaentregas.core.exception.EncomendaValidationException;
import com.breudes.gerenciaentregas.core.port.NotificacaoPublisher;
import com.breudes.gerenciaentregas.core.repository.EncomendaRepository;

public class CriarEncomendaUseCase {
    private final EncomendaRepository encomendaRepository;
    private final NotificacaoPublisher notificacaoPublisher;

    public CriarEncomendaUseCase(
            EncomendaRepository encomendaRepository,
            NotificacaoPublisher notificacaoPublisher
    ) {
        this.encomendaRepository = encomendaRepository;
        this.notificacaoPublisher = notificacaoPublisher;
    }

    public Encomenda execute(Encomenda encomenda){
        validaEncomenda(encomenda);
        Encomenda encomendaCriada = encomendaRepository.criarEncomenda(encomenda);
        if (encomendaCriada == null) {
            return null;
        }
        return encomendaCriada;
    }

    public void validaEncomenda(Encomenda encomenda){
        if(encomenda == null){
            throw new EncomendaValidationException("encomenda", "Encomenda não pode ser nula.");
        }
        if(encomenda.getDescricao() == null){
            throw new EncomendaValidationException("descrição", "Descrição é obrigatória e não pode ser nula.");
        }
        if(encomenda.getDescricao().isBlank()){
            throw new EncomendaValidationException("descrição", "Descrição é obrigatória e não pode ser vazia.");
        }
        if(encomenda.getMoradorDestinatario() == null){
            throw new EncomendaValidationException("morador", "Morador é obrigatório e não pode ser nulo.");
        }
        if(encomenda.getMoradorDestinatario().getNome() == null){
            throw new EncomendaValidationException("nome de morador", "Nome de morador é obrigatório e não pode ser nulo.");
        }
        if(encomenda.getMoradorDestinatario().getNome().isBlank()){
            throw new EncomendaValidationException("nome de morador", "Nome de morador é obrigatório e não pode ser vazio.");
        }
        if (encomenda.getMoradorDestinatario().getEmail() == null
                || encomenda.getMoradorDestinatario().getEmail().isBlank()) {
            throw new EncomendaValidationException("email", "E-mail do morador é obrigatório e não pode ser vazio.");
        }
    }
}
