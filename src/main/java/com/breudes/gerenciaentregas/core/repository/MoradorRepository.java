package com.breudes.gerenciaentregas.core.repository;

import com.breudes.gerenciaentregas.core.domain.Morador;

public interface MoradorRepository {
    Morador criarMorador(Morador novoMorador);
    Morador buscarMorador(Long idMorador);
    Morador atualizarMorador(Morador moradorExistente);
}
