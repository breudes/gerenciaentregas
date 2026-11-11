package com.breudes.gerenciaentregas.infrastructures.messaging;

import com.breudes.gerenciaentregas.core.event.NotificacaoEmail;
import com.breudes.gerenciaentregas.core.port.NotificacaoPublisher;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class RabbitMQNotificacaoPublisher implements NotificacaoPublisher {
    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final String exchange;
    private final String routingKey;

    public RabbitMQNotificacaoPublisher(
            RabbitTemplate rabbitTemplate,
            @Value("${app.rabbitmq.exchange}") String exchange,
            @Value("${app.rabbitmq.routing-key}") String routingKey
    ) {
        this.rabbitTemplate = rabbitTemplate;
        this.exchange = exchange;
        this.routingKey = routingKey;
    }

    @Override
    public void publicar(NotificacaoEmail notificacao) {
        try {
            String mensagem = objectMapper.writeValueAsString(notificacao);
            rabbitTemplate.convertAndSend(exchange, routingKey, mensagem);
        } catch (JsonProcessingException exception) {
            throw new IllegalStateException("Não foi possível serializar a notificação de e-mail.", exception);
        }
    }
}