package com.breudes.gerenciaentregas.interfaces.dto.encomenda;

import com.breudes.gerenciaentregas.interfaces.dto.morador.MoradorRequestDTO;

public record EncomendaRequestDto(
    String descricao,
    MoradorRequestDTO moradorRequestDTO
) {
}
