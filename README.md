# Gerência de Entregas

Microsserviço Spring Boot responsável pelo cadastro e acompanhamento de moradores e encomendas. Expõe uma API REST e publica eventos de notificação no RabbitMQ.

## Requisitos

- Java 21
- Docker e Docker Compose, para executar as dependências (PostgreSQL e RabbitMQ)

## Executar com o projeto completo

Na raiz do repositório, configure as credenciais SMTP no `.env` conforme o [README principal](../README.md) e execute:

```bash
docker compose up --build
```

A API ficará disponível em `http://localhost:8080`. A interface Swagger está em [`http://localhost:8080/swagger-ui/index.html`](http://localhost:8080/swagger-ui/index.html), e o documento OpenAPI em [`http://localhost:8080/v3/api-docs`](http://localhost:8080/v3/api-docs).

## Executar localmente

O serviço depende de PostgreSQL e RabbitMQ. Com esses serviços disponíveis, configure a conexão com o banco local e o RabbitMQ:

```text
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/encomendas
DB_NAME=encomendas
DB_USER=postgres
DB_PASS=postgres
SPRING_RABBITMQ_HOST=localhost
SPRING_RABBITMQ_PORT=5672
SPRING_RABBITMQ_USERNAME=guest
SPRING_RABBITMQ_PASSWORD=guest
```

O banco `encomendas` deve existir no PostgreSQL. Em seguida, execute na pasta `gerenciaentregas`:

```bash
.\mvnw.cmd spring-boot:run
```

No PowerShell, o comando acima usa o Maven Wrapper do projeto. A aplicação usa a porta `8081` por padrão quando iniciada diretamente.

## Endpoints

Base da API: `/api`.

| Método | Caminho | Descrição |
|---|---|---|
| `POST` | `/api/moradores` | Cadastra um morador |
| `PUT` | `/api/moradores?telefoneAtual={telefone}` | Atualiza os dados de um morador localizado pelo telefone atual |
| `POST` | `/api/encomendas` | Cadastra uma encomenda e solicita uma notificação |
| `PATCH` | `/api/encomendas/confirma-notificacao?idEncomenda={id}` | Confirma o recebimento da notificação |
| `PATCH` | `/api/encomendas/retirada?idEncomenda={id}` | Confirma a retirada da encomenda |

### Exemplo: cadastrar uma encomenda

```bash
curl -X POST http://localhost:8080/api/encomendas \
  -H "Content-Type: application/json" \
  -d '{
    "descricao": "Pacote de livros",
    "moradorRequestDTO": {
      "nome": "Maria Silva",
      "telefone": "11999999999",
      "email": "maria@example.com",
      "apartamento": "101",
      "bloco": "A"
    }
  }'
```

O e-mail do destinatário é necessário para cadastrar uma encomenda e enviar a notificação. A API também permite consultar e testar os formatos de requisição e resposta pelo Swagger UI.

## Autor

[Brenda Silva](https://www.linkedin.com/in/breudes/)

Este serviço de gerenciamento de entregas residenciais faz parte do projeto substitutivo da fase 4 da pós tech Java (FIAP).