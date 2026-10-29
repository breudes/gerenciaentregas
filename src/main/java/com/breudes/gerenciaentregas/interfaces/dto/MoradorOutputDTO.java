package com.breudes.gerenciaentregas.interfaces.dto;

public record MoradorOutputDTO(
        Long id,
        String nome,
        String telefone,
        String apartamento,
        String bloco
) {
}
