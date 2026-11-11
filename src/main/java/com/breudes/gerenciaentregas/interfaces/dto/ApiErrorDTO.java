package com.breudes.gerenciaentregas.interfaces.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Detalhes de uma falha de validação da requisição.")
public record ApiErrorDTO(
	@Schema(description = "Campo associado ao erro", example = "email")
	String atributo,
	@Schema(description = "Descrição do erro", example = "E-mail do morador é obrigatório.")
	String mensagem
) {
}
