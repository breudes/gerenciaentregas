package com.breudes.gerenciaentregas.core.port;

import com.breudes.gerenciaentregas.core.event.NotificacaoEmail;

public interface NotificacaoPublisher {
    void publicar(NotificacaoEmail notificacao);
}