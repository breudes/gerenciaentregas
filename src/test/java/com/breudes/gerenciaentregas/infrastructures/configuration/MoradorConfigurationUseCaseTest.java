package com.breudes.gerenciaentregas.infrastructures.configuration;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

import com.breudes.gerenciaentregas.core.repository.MoradorRepository;
import org.junit.jupiter.api.Test;

class MoradorConfigurationUseCaseTest {

    @Test
    void deveCriarBeansDeMorador() {
        MoradorRepository moradorRepository = mock(MoradorRepository.class);
        MoradorConfigurationUseCase configuration = new MoradorConfigurationUseCase(moradorRepository);

        assertNotNull(configuration.criarMoradorUseCase());
        assertNotNull(configuration.atualizarMoradorUseCase());
    }
}
