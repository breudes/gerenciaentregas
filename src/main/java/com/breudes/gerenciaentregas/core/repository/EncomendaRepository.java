package com.breudes.gerenciaentregas.core.repository;

import com.breudes.gerenciaentregas.core.domain.Encomenda;

public interface EncomendaRepository {
    Encomenda criarEncomenda(Encomenda encomenda);
    Encomenda confirmarEntregaEncomenda(Encomenda encomenda);
}
