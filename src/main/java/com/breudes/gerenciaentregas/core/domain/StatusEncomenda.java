package com.breudes.gerenciaentregas.core.domain;

public enum StatusEncomenda {
    PENDENTE ("Pendente"),
    NOTIFICACAO_ENVIADA("Notificação enviada"),
    NOTIFICACAO_RECEBIDA("Notificação recebida"),
    ENTREGUE("Entregue");

    StatusEncomenda(String pendente) {
    }
}
