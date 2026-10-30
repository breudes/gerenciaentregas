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
    public Optional<MoradorJpa> buscarMorador(String telefone) {
        if (telefone == null) {
            return Optional.empty();
        }

        return springDataJpaMorador.findByTelefone(telefone);
    }

    @Override
    public Morador atualizarMorador(String telefoneAtual, Morador moradorExistente) {
        if (moradorExistente == null || (telefoneAtual == null || telefoneAtual.isBlank())) {
            return null;
        }

        Optional<MoradorJpa> optMoradorEncontradoJpa = buscarMorador(telefoneAtual);
        MoradorJpa moradorEncontradoJpa = null;

        if(optMoradorEncontradoJpa.isPresent()){
            moradorEncontradoJpa = optMoradorEncontradoJpa.get();

            if(telefoneAtual.contains(moradorExistente.getTelefone())){
                moradorEncontradoJpa.setNome(moradorExistente.getNome());
                moradorEncontradoJpa.setApartamento(moradorExistente.getApartamento());
                moradorEncontradoJpa.setBloco(moradorExistente.getBloco());
            } else {
                Optional<MoradorJpa> moradorComMesmoTelefone = springDataJpaMorador.findByTelefone(moradorExistente.getTelefone());
                if (moradorComMesmoTelefone.isPresent() && !moradorComMesmoTelefone.get().getId().equals(moradorExistente.getId())) {
                    throw new IllegalArgumentException("Telefone que deseja inserir já está registrado em nosso sistema.");
                }

                moradorEncontradoJpa.setNome(moradorExistente.getNome());
                moradorEncontradoJpa.setTelefone(moradorExistente.getTelefone());
                moradorEncontradoJpa.setApartamento(moradorExistente.getApartamento());
                moradorEncontradoJpa.setBloco(moradorExistente.getBloco());
            }

            MoradorJpa moradorAtualizado = springDataJpaMorador.save(moradorEncontradoJpa);
            return MoradorMapper.converteJpaParaEntity(moradorAtualizado);
        } else {
            throw new IllegalArgumentException("Morador não foi encontrado com este telefone atual.");
        }
    }
}
