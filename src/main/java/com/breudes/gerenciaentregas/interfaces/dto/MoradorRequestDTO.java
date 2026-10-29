package com.breudes.gerenciaentregas.interfaces.dto;

import com.breudes.gerenciaentregas.core.domain.Morador;

public record MoradorRequestDTO(
        String nome,
        String telefone,
        String apartamento,
        String bloco
) {
}
