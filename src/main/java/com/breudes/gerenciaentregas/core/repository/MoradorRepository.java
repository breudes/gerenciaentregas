package com.breudes.gerenciaentregas.core.repository;

import com.breudes.gerenciaentregas.core.domain.Morador;
import com.breudes.gerenciaentregas.infrastructures.entity.MoradorJpa;

import java.util.Optional;

public interface MoradorRepository {
    Morador criarMorador(Morador novoMorador);
    Optional<MoradorJpa> buscarMorador(String telefone);
    Morador atualizarMorador(String telefoneAtual,Morador moradorExistente);
}
