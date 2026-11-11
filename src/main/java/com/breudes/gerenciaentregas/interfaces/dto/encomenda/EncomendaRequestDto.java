package com.breudes.gerenciaentregas.interfaces.dto.encomenda;

import com.breudes.gerenciaentregas.interfaces.dto.morador.MoradorRequestDTO;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dados necessários para cadastrar uma encomenda e seu destinatário.")
public record EncomendaRequestDto(
    @Schema(description = "Descrição ou identificação da encomenda", example = "Pacote de livros")
    String descricao,
    @Schema(description = "Dados do morador destinatário")
    MoradorRequestDTO moradorRequestDTO
) {
}
