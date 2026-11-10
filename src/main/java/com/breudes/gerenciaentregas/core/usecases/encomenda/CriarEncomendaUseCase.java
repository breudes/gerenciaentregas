package com.breudes.gerenciaentregas.core.usecases.encomenda;

import com.breudes.gerenciaentregas.core.domain.Encomenda;
import com.breudes.gerenciaentregas.core.exception.EncomendaValidationException;
import com.breudes.gerenciaentregas.core.repository.EncomendaRepository;

public class CriarEncomendaUseCase {
    private final EncomendaRepository encomendaRepository;

    public CriarEncomendaUseCase(EncomendaRepository encomendaRepository) {
        this.encomendaRepository = encomendaRepository;
    }

    public Encomenda execute(Encomenda encomenda){
        validaEncomenda(encomenda);
        return encomendaRepository.criarEncomenda(encomenda);
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
    }
}
