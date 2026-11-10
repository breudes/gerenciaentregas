package com.breudes.gerenciaentregas.infrastructures.enums;

public enum StatusEncomendaJpa {
    PENDENTE ("Pendente"),
    NOTIFICACAO_ENVIADA("Notificação enviada"),
    NOTIFICACAO_RECEBIDA("Notificação recebida"),
    ENTREGUE("Entregue");

    StatusEncomendaJpa(String pendente) {
    }
}
