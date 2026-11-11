package com.breudes.gerenciaentregas.infrastructures.configuration;

import com.breudes.gerenciaentregas.core.repository.EncomendaRepository;
import com.breudes.gerenciaentregas.core.port.NotificacaoPublisher;
import com.breudes.gerenciaentregas.core.usecases.encomenda.ConfirmarNotificacaoDeEncomendaUseCase;
import com.breudes.gerenciaentregas.core.usecases.encomenda.ConfirmarRetiradaDeEncomendaUseCase;
import com.breudes.gerenciaentregas.core.usecases.encomenda.CriarEncomendaUseCase;
import com.breudes.gerenciaentregas.core.usecases.encomenda.EnviaNotificacaoDeEncomendaUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EncomendaConfigurationUseCase {
    private final EncomendaRepository encomendaRepository;
    private final NotificacaoPublisher notificacaoPublisher;

    public EncomendaConfigurationUseCase(
            EncomendaRepository encomendaRepository,
            NotificacaoPublisher notificacaoPublisher
    ) {
        this.encomendaRepository = encomendaRepository;
        this.notificacaoPublisher = notificacaoPublisher;
    }

    @Bean
    public CriarEncomendaUseCase criarEncomendaUseCase(){
        return new CriarEncomendaUseCase(encomendaRepository, notificacaoPublisher);
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
