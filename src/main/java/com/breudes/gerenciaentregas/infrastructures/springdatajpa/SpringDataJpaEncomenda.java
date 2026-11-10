package com.breudes.gerenciaentregas.infrastructures.springdatajpa;

import com.breudes.gerenciaentregas.infrastructures.entity.EncomendaJpa;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface SpringDataJpaEncomenda extends JpaRepository<EncomendaJpa, Long> {
    Optional<EncomendaJpa> findByDescricao(String descricao);
}
