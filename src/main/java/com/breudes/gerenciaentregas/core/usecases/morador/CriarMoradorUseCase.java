package com.breudes.gerenciaentregas.core.usecases.morador;

import com.breudes.gerenciaentregas.core.domain.Morador;
import com.breudes.gerenciaentregas.core.exception.MoradorValidationException;
import com.breudes.gerenciaentregas.core.repository.MoradorRepository;

public class CriarMoradorUseCase {
    private final MoradorRepository moradorRepository;

    public CriarMoradorUseCase(MoradorRepository moradorRepository) {
        this.moradorRepository = moradorRepository;
    }

    public Morador execute(Morador morador){
        validarMorador(morador);
        return moradorRepository.criarMorador(morador);
    }

    public void validarMorador(Morador morador) {
        if (morador == null) {
            throw new MoradorValidationException("morador", "Morador não pode ser nulo.");
        }

        if (morador.getTelefone() == null || morador.getTelefone().isBlank()) {
            throw new MoradorValidationException("telefone", "Telefone do morador é obrigatório.");
        }

        if (morador.getApartamento() == null || morador.getApartamento().isBlank()) {
            throw new MoradorValidationException("apartamento", "Apartamento do morador é obrigatório.");
        }

        if (morador.getBloco() == null || morador.getBloco().isBlank()) {
            throw new MoradorValidationException("bloco", "Bloco do morador é obrigatório.");
        }
    }
}
