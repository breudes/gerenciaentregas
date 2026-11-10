package com.breudes.gerenciaentregas.infrastructures.configuration;

import com.breudes.gerenciaentregas.core.repository.EncomendaRepository;
import com.breudes.gerenciaentregas.core.usecases.encomenda.ConfirmarNotificacaoDeEncomendaUseCase;
import com.breudes.gerenciaentregas.core.usecases.encomenda.ConfirmarRetiradaDeEncomendaUseCase;
import com.breudes.gerenciaentregas.core.usecases.encomenda.CriarEncomendaUseCase;
import com.breudes.gerenciaentregas.core.usecases.encomenda.EnviaNotificacaoDeEncomendaUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EncomendaConfigurationUseCase {
    private final EncomendaRepository encomendaRepository;

    public EncomendaConfigurationUseCase(EncomendaRepository encomendaRepository) {
        this.encomendaRepository = encomendaRepository;
    }

    @Bean
    public CriarEncomendaUseCase criarEncomendaUseCase(){
        return new CriarEncomendaUseCase(encomendaRepository);
    }

    @Bean
    public EnviaNotificacaoDeEncomendaUseCase enviaNotificacaoDeEncomendaUseCase(){
        return new EnviaNotificacaoDeEncomendaUseCase(encomendaRepository);
    }

    @Bean
    public ConfirmarNotificacaoDeEncomendaUseCase confirmarNotificacaoDeEncomendaUseCase(){
        return new ConfirmarNotificacaoDeEncomendaUseCase(encomendaRepository);
    }

    @Bean
    public ConfirmarRetiradaDeEncomendaUseCase confirmarRetiradaDeEncomendaUseCase(){
        return new ConfirmarRetiradaDeEncomendaUseCase(encomendaRepository);
    }
}
