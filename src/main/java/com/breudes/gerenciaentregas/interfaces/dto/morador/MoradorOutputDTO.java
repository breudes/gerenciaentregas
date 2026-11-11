package com.breudes.gerenciaentregas.interfaces.dto.morador;

public record MoradorOutputDTO(
        String nome,
        String telefone,
                String email,
        String apartamento,
        String bloco
) {
        public MoradorOutputDTO(String nome, String telefone, String apartamento, String bloco) {
                this(nome, telefone, null, apartamento, bloco);
        }
}
