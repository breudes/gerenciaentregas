package com.breudes.gerenciaentregas.infrastructures.repository;

import com.breudes.gerenciaentregas.core.domain.Encomenda;
import com.breudes.gerenciaentregas.core.domain.Morador;
import com.breudes.gerenciaentregas.core.event.NotificacaoEmail;
import com.breudes.gerenciaentregas.core.port.NotificacaoPublisher;
import com.breudes.gerenciaentregas.core.repository.EncomendaRepository;
import com.breudes.gerenciaentregas.infrastructures.entity.EncomendaJpa;
import com.breudes.gerenciaentregas.infrastructures.entity.MoradorJpa;
import com.breudes.gerenciaentregas.infrastructures.enums.StatusEncomendaJpa;
import com.breudes.gerenciaentregas.infrastructures.springdatajpa.SpringDataJpaEncomenda;
import com.breudes.gerenciaentregas.infrastructures.springdatajpa.SpringDataJpaMorador;
import com.breudes.gerenciaentregas.interfaces.mapper.EncomendaMapper;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

import static com.breudes.gerenciaentregas.interfaces.mapper.EncomendaMapper.*;

@Repository
public class EncomendaRepositoryJpa implements EncomendaRepository {
    private final SpringDataJpaEncomenda springDataJpaEncomenda;
    private final SpringDataJpaMorador springDataJpaMorador;
    private final NotificacaoPublisher notificacaoPublisher;

    public EncomendaRepositoryJpa(SpringDataJpaEncomenda springDataJpaEncomenda, SpringDataJpaMorador springDataJpaMorador, NotificacaoPublisher notificacaoPublisher) {
        this.springDataJpaEncomenda = springDataJpaEncomenda;
        this.springDataJpaMorador = springDataJpaMorador;
        this.notificacaoPublisher = notificacaoPublisher;
    }

    @Override
    public Encomenda criarEncomenda(Encomenda encomenda) {
        if (encomenda == null) {
            return null;
        }
        // verifica se a encomenda já existe pela a descrição
        Optional<EncomendaJpa> encomendaEncontrada = springDataJpaEncomenda.findByDescricao(encomenda.getDescricao());
       if(encomendaEncontrada.isPresent()){
            throw new IllegalArgumentException("Encomenda já registrada");
        } else {
            EncomendaJpa encomendaJpa = converteDomainParaJpa(encomenda);
            // verifica se o morador já existe pelo o telefone
            Optional<MoradorJpa> moradorEncontrado = springDataJpaMorador.findByTelefone(encomenda.getMoradorDestinatario().getTelefone());

            if(moradorEncontrado.isPresent()){
                MoradorJpa moradorEncontradoJpa = moradorEncontrado.get();
                if(Objects.equals(moradorEncontradoJpa.getNome(), encomenda.getMoradorDestinatario().getNome())){
                    encomendaJpa.setMoradorDestinatarioJpa(moradorEncontrado.get());
                } else
                    throw new IllegalArgumentException("Telefone já registrado para outro morador.");
            }

            EncomendaJpa encomendaSalva = springDataJpaEncomenda.save(encomendaJpa);
            Encomenda encomendaSalvaDomain = EncomendaMapper.converteJpaParaDomain(encomendaSalva);

           Morador destinatario = encomendaSalvaDomain.getMoradorDestinatario();
            if (encomendaSalva.getId() == null) {
                throw new IllegalStateException("A encomenda criada precisa ter um ID para gerar a notificação.");
            }

            notificacaoPublisher.publicar(new NotificacaoEmail(
                    encomendaSalva.getId(),
                    destinatario.getEmail(),
                    destinatario.getNome(),
                    encomendaSalva.getDescricao(),
                    destinatario.getApartamento(),
                    destinatario.getBloco()
            ));

            Encomenda encomendaNotificada = this.enviaNotificacaoEncomenda(encomendaSalva.getId());
            enviaNotificacaoEncomenda(encomendaSalva.getId());
            return encomendaNotificada != null ? encomendaNotificada : encomendaSalvaDomain;
        }
    }

    @Override
    public Encomenda buscarEncomenda(Long idEncomenda) {
        if (idEncomenda == null) {
            return null;
        }

        Optional<EncomendaJpa> encomendaEncontrada = springDataJpaEncomenda.findById(idEncomenda);
        return encomendaEncontrada.map(EncomendaMapper::converteJpaParaDomain).orElse(null);
    }

    @Override
    public Encomenda enviaNotificacaoEncomenda(Long idEncomenda){
        if (idEncomenda == null) {
            return null;
        }

        Optional<EncomendaJpa> encomendaEncontrada = springDataJpaEncomenda.findById(idEncomenda);
        if (encomendaEncontrada.isEmpty()) {
            return null;
        }

        EncomendaJpa encomendaJpa = encomendaEncontrada.get();
        if (encomendaJpa.getStatus() == StatusEncomendaJpa.PENDENTE) {
            encomendaJpa.setStatus(StatusEncomendaJpa.NOTIFICACAO_ENVIADA);
            springDataJpaEncomenda.save(encomendaJpa);
            return EncomendaMapper.converteJpaParaDomain(encomendaJpa);
        }

        return null;
    }

    @Override
    public Encomenda confirmaNotificacaoEncomenda(Long idEncomenda) {
        if (idEncomenda == null) {
            return null;
        }

        Optional<EncomendaJpa> encomendaEncontrada = springDataJpaEncomenda.findById(idEncomenda);
        if (encomendaEncontrada.isEmpty()) {
            return null;
        }

        EncomendaJpa encomendaJpa = encomendaEncontrada.get();
        if (encomendaJpa.getStatus() == StatusEncomendaJpa.NOTIFICACAO_ENVIADA) {
            encomendaJpa.setStatus(StatusEncomendaJpa.NOTIFICACAO_RECEBIDA);
            springDataJpaEncomenda.save(encomendaJpa);
            return EncomendaMapper.converteJpaParaDomain(encomendaJpa);
        }
        return null;
    }

    @Override
    public Encomenda confirmarEntregaEncomenda(Encomenda encomenda) {
        if (encomenda == null || encomenda.getId() == null) {
            return null;
        }

        EncomendaJpa encomendaJpa = springDataJpaEncomenda.findById(encomenda.getId()).orElse(null);
        if (encomendaJpa == null) {
            return null;
        }

        encomendaJpa.setStatus(StatusEncomendaJpa.ENTREGUE);
        encomendaJpa.setDataDeEntrega(LocalDateTime.now());
        encomendaJpa.setAtivo(false);

        EncomendaJpa encomendaAtualizada = springDataJpaEncomenda.save(encomendaJpa);
        return converteJpaParaDomain(encomendaAtualizada);
    }
}
