package com.breudes.gerenciaentregas.interfaces.mapper;

import com.breudes.gerenciaentregas.core.domain.Morador;
import com.breudes.gerenciaentregas.infrastructures.entity.MoradorJpa;
import com.breudes.gerenciaentregas.interfaces.dto.morador.MoradorOutputDTO;
import com.breudes.gerenciaentregas.interfaces.dto.morador.MoradorRequestDTO;

public class MoradorMapper {
    public static MoradorJpa converteEntityparaJpa(Morador morador){
        if (morador == null) {
            return null;
        }

        return new MoradorJpa(
                morador.getId(),
                morador.getNome(),
                morador.getTelefone(),
                morador.getApartamento(),
                morador.getBloco());
    }

    public static Morador converteJpaParaEntity(MoradorJpa moradorJpa){
        if (moradorJpa == null) {
            return null;
        }

        return new Morador(
                moradorJpa.getId(),
                moradorJpa.getNome(),
                moradorJpa.getTelefone(),
                moradorJpa.getApartamento(),
                moradorJpa.getBloco()
        );
    }

    public static Morador converteRequestDtoParaEntity(MoradorRequestDTO moradorRequestDTO){
        if (moradorRequestDTO == null) {
            return null;
        }

        return new Morador(
                null,
                moradorRequestDTO.nome(),
                moradorRequestDTO.telefone(),
                moradorRequestDTO.apartamento(),
                moradorRequestDTO.bloco()
        );
    }

    public static Morador converteOutputDtoParaEntity(MoradorOutputDTO moradorOutputDTO){
        if (moradorOutputDTO == null) {
            return null;
        }

        return new Morador(
                null,
                moradorOutputDTO.nome(),
                moradorOutputDTO.telefone(),
                moradorOutputDTO.apartamento(),
                moradorOutputDTO.bloco()
        );
    }
}
