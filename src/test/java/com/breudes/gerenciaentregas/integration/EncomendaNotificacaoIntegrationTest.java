package com.breudes.gerenciaentregas.integration;

import com.breudes.gerenciaentregas.infrastructures.entity.EncomendaJpa;
import com.breudes.gerenciaentregas.infrastructures.enums.StatusEncomendaJpa;
import com.breudes.gerenciaentregas.infrastructures.springdatajpa.SpringDataJpaEncomenda;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Testcontainers(disabledWithoutDocker = true)
@SpringBootTest
@AutoConfigureMockMvc
class EncomendaNotificacaoIntegrationTest {
    private static final String QUEUE = "integration.notificacao.email";
    private static final String EXCHANGE = "integration.notificacao.exchange";
    private static final String ROUTING_KEY = "integration.notificacao.email";

    @Container
    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:18-alpine")
            .withDatabaseName("encomendas_test")
            .withUsername("test")
            .withPassword("test");

    @Container
    private static final RabbitMQContainer rabbit = new RabbitMQContainer("rabbitmq:3-management-alpine");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
        registry.add("spring.rabbitmq.host", rabbit::getHost);
        registry.add("spring.rabbitmq.port", rabbit::getAmqpPort);
        registry.add("spring.rabbitmq.username", rabbit::getAdminUsername);
        registry.add("spring.rabbitmq.password", rabbit::getAdminPassword);
        registry.add("app.rabbitmq.queue", () -> QUEUE);
        registry.add("app.rabbitmq.exchange", () -> EXCHANGE);
        registry.add("app.rabbitmq.routing-key", () -> ROUTING_KEY);
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private SpringDataJpaEncomenda encomendaRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void devePersistirEncomendaEPublicarNotificacaoNoRabbit() throws Exception {
        String request = """
                {
                  "descricao": "Pacote de integração",
                  "moradorRequestDTO": {
                    "nome": "Maria Silva",
                    "telefone": "11999990001",
                    "email": "maria@example.com",
                    "apartamento": "101",
                    "bloco": "A"
                  }
                }
                """;

        String responseBody = mockMvc.perform(post("/api/encomendas")
                        .contentType(APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        JsonNode response = objectMapper.readTree(responseBody);
        long encomendaId = response.path("id").asLong();
        Message message = rabbitTemplate.receive(QUEUE, 5_000);
        assertNotNull(message, "A mensagem não foi encontrada na fila RabbitMQ.");

        JsonNode notification = objectMapper.readTree(new String(message.getBody(), StandardCharsets.UTF_8));
        EncomendaJpa persisted = encomendaRepository.findById(encomendaId).orElseThrow();

        assertAll(
                () -> assertEquals(StatusEncomendaJpa.NOTIFICACAO_ENVIADA, persisted.getStatus()),
                () -> assertEquals(encomendaId, notification.path("encomendaId").asLong()),
                () -> assertEquals("maria@example.com", notification.path("emailDestinatario").asText()),
                () -> assertEquals("Maria Silva", notification.path("nomeDestinatario").asText()),
                () -> assertEquals("Pacote de integração", notification.path("descricaoEncomenda").asText()),
                () -> assertEquals("101", notification.path("apartamento").asText()),
                () -> assertEquals("A", notification.path("bloco").asText())
        );
    }
}