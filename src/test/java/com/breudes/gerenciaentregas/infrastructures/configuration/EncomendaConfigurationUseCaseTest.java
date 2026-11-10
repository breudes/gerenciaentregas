package com.breudes.gerenciaentregas.infrastructures.configuration;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

import com.breudes.gerenciaentregas.core.repository.EncomendaRepository;
import org.junit.jupiter.api.Test;

class EncomendaConfigurationUseCaseTest {

    @Test
    void deveCriarBeansDeEncomenda() {
        EncomendaRepository encomendaRepository = mock(EncomendaRepository.class);
        EncomendaConfigurationUseCase configuration = new EncomendaConfigurationUseCase(encomendaRepository);

        assertNotNull(configuration.criarEncomendaUseCase());
        assertNotNull(configuration.enviaNotificacaoDeEncomendaUseCase());
        assertNotNull(configuration.confirmarNotificacaoDeEncomendaUseCase());
        assertNotNull(configuration.confirmarRetiradaDeEncomendaUseCase());
    }
}
