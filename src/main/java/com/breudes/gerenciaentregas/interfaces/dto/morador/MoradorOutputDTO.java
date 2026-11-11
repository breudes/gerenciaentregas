package com.breudes.gerenciaentregas.interfaces.dto.morador;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dados atualizados de um morador.")
public record MoradorOutputDTO(
        @Schema(description = "Nome completo", example = "Maria Silva")
        String nome,
        @Schema(description = "Telefone de contato", example = "11999999999")
        String telefone,
        @Schema(description = "E-mail para receber notificações", example = "maria@example.com")
                String email,
        @Schema(description = "Número do apartamento", example = "101")
        String apartamento,
        @Schema(description = "Bloco do apartamento", example = "A")
        String bloco
) {
        public MoradorOutputDTO(String nome, String telefone, String apartamento, String bloco) {
                this(nome, telefone, null, apartamento, bloco);
        }
}
