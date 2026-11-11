package com.breudes.gerenciaentregas.infrastructures.repository;

import com.breudes.gerenciaentregas.core.domain.Encomenda;
import com.breudes.gerenciaentregas.core.domain.Morador;
import com.breudes.gerenciaentregas.core.domain.StatusEncomenda;
import com.breudes.gerenciaentregas.core.event.NotificacaoEmail;
import com.breudes.gerenciaentregas.core.port.NotificacaoPublisher;
import com.breudes.gerenciaentregas.infrastructures.entity.EncomendaJpa;
import com.breudes.gerenciaentregas.infrastructures.springdatajpa.SpringDataJpaEncomenda;
import com.breudes.gerenciaentregas.infrastructures.springdatajpa.SpringDataJpaMorador;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class EncomendaRepositoryJpaTest {
    private final SpringDataJpaEncomenda springDataJpaEncomenda = mock(SpringDataJpaEncomenda.class);
    private final SpringDataJpaMorador springDataJpaMorador = mock(SpringDataJpaMorador.class);
    private final NotificacaoPublisher notificacaoPublisher = mock(NotificacaoPublisher.class);
    private final EncomendaRepositoryJpa repository = new EncomendaRepositoryJpa(
            springDataJpaEncomenda,
            springDataJpaMorador,
            notificacaoPublisher
    );

    @Test
    void devePublicarUmaNotificacaoComOsDadosDaEncomendaCriada() {
        Morador morador = new Morador(1L, "Maria Silva", "11999999999", "maria@example.com", "101", "A");
        Encomenda encomenda = new Encomenda(
                null, "Pacote de teste", morador, null, LocalDateTime.now(), null, true, StatusEncomenda.PENDENTE
        );
        when(springDataJpaEncomenda.findByDescricao("Pacote de teste")).thenReturn(Optional.empty());
        when(springDataJpaMorador.findByTelefone("11999999999")).thenReturn(Optional.empty());
                AtomicReference<EncomendaJpa> savedEntity = new AtomicReference<>();
        when(springDataJpaEncomenda.save(any(EncomendaJpa.class))).thenAnswer(invocation -> {
            EncomendaJpa entity = invocation.getArgument(0);
            entity.setId(42L);
                        savedEntity.set(entity);
            return entity;
        });
                when(springDataJpaEncomenda.findById(42L)).thenAnswer(invocation -> Optional.of(savedEntity.get()));

        Encomenda resultado = repository.criarEncomenda(encomenda);

        ArgumentCaptor<NotificacaoEmail> notificacaoCaptor = ArgumentCaptor.forClass(NotificacaoEmail.class);
        verify(notificacaoPublisher, times(1)).publicar(notificacaoCaptor.capture());
        verify(springDataJpaEncomenda, times(2)).save(any(EncomendaJpa.class));
        assertAll(
                () -> assertEquals(42L, resultado.getId()),
                () -> assertEquals(StatusEncomenda.NOTIFICACAO_ENVIADA, resultado.getStatus()),
                () -> assertEquals(new NotificacaoEmail(
                        42L, "maria@example.com", "Maria Silva", "Pacote de teste", "101", "A"
                ), notificacaoCaptor.getValue())
        );
    }
}