package com.breudes.gerenciaentregas.interfaces.dto.morador;

public record MoradorRequestDTO(
        String nome,
        String telefone,
                String email,
        String apartamento,
        String bloco
) {
        public MoradorRequestDTO(String nome, String telefone, String apartamento, String bloco) {
                this(nome, telefone, null, apartamento, bloco);
        }
}
