package com.breudes.gerenciaentregas.infrastructures.springdatajpa;

import com.breudes.gerenciaentregas.infrastructures.entity.MoradorJpa;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface SpringDataJpaMorador extends JpaRepository<MoradorJpa, Long> {
    Optional<MoradorJpa> findByTelefone(String telefone);
}
