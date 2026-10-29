package com.breudes.gerenciaentregas.infrastructures.repository;

import com.breudes.gerenciaentregas.core.domain.Morador;
import com.breudes.gerenciaentregas.core.repository.MoradorRepository;
import com.breudes.gerenciaentregas.infrastructures.entity.MoradorJpa;
import com.breudes.gerenciaentregas.infrastructures.springdatajpa.SpringDataJpaMorador;
import com.breudes.gerenciaentregas.interfaces.mapper.MoradorMapper;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class MoradorRepositoryJpa implements MoradorRepository {
    private SpringDataJpaMorador springDataJpaMorador;

    public MoradorRepositoryJpa(SpringDataJpaMorador springDataJpaMorador) {
        this.springDataJpaMorador = springDataJpaMorador;
    }

    @Override
    public Morador criarMorador(Morador novoMorador) {
        MoradorJpa novoMoradorJpa = MoradorMapper.converteEntityparaJpa(novoMorador);
        MoradorJpa moradorRegistrado = null;

        // verifica se morador já existe pelo o telefone
        Optional<MoradorJpa> moradorEncontrado = springDataJpaMorador.findByTelefone(novoMorador.getTelefone());

        if(moradorEncontrado.isEmpty()){
            moradorRegistrado = springDataJpaMorador.save(novoMoradorJpa);
        } else {
            throw new IllegalArgumentException("Telefone já registrado.");
        }
        return MoradorMapper.converteJpaParaEntity(moradorRegistrado);
    }

    @Override
    public Morador buscarMorador(Long idMorador) {
        return null;
    }

    @Override
    public Morador atualizarMorador(Morador moradorExistente) {
        return null;
    }
}
