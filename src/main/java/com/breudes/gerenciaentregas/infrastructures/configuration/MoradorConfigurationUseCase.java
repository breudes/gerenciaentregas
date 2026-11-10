package com.breudes.gerenciaentregas.infrastructures.configuration;

import com.breudes.gerenciaentregas.core.domain.Morador;
import com.breudes.gerenciaentregas.core.repository.MoradorRepository;
import com.breudes.gerenciaentregas.core.usecases.morador.AtualizarMoradorUseCase;
import com.breudes.gerenciaentregas.core.usecases.morador.CriarMoradorUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MoradorConfigurationUseCase {
    private final MoradorRepository moradorRepository;

    public MoradorConfigurationUseCase(MoradorRepository moradorRepository) {
        this.moradorRepository = moradorRepository;
    }

    @Bean
    public CriarMoradorUseCase criarMoradorUseCase(){
        return new CriarMoradorUseCase(moradorRepository);
    }

    @Bean
    public AtualizarMoradorUseCase atualizarMoradorUseCase(){
        return new AtualizarMoradorUseCase(moradorRepository);
    }
}
