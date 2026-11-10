package com.breudes.gerenciaentregas.interfaces.dto.morador;

public record MoradorRequestDTO(
        String nome,
        String telefone,
        String apartamento,
        String bloco
) {
}
