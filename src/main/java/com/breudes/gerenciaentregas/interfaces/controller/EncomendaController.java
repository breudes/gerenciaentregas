package com.breudes.gerenciaentregas.interfaces.controller;

import com.breudes.gerenciaentregas.core.domain.Encomenda;
import com.breudes.gerenciaentregas.core.usecases.encomenda.ConfirmarNotificacaoDeEncomendaUseCase;
import com.breudes.gerenciaentregas.core.usecases.encomenda.ConfirmarRetiradaDeEncomendaUseCase;
import com.breudes.gerenciaentregas.core.usecases.encomenda.CriarEncomendaUseCase;
import com.breudes.gerenciaentregas.interfaces.dto.encomenda.EncomendaRequestDto;
import com.breudes.gerenciaentregas.interfaces.mapper.EncomendaMapper;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/encomendas")
public class EncomendaController {
    private final CriarEncomendaUseCase criarEncomendaUseCase;
    private final ConfirmarNotificacaoDeEncomendaUseCase confirmarNotificacaoDeEncomendaUseCase;
    private final ConfirmarRetiradaDeEncomendaUseCase confirmarRetiradaDeEncomendaUseCase;

    public EncomendaController(CriarEncomendaUseCase criarEncomendaUseCase,
                               ConfirmarNotificacaoDeEncomendaUseCase confirmarNotificacaoDeEncomendaUseCase,
                               ConfirmarRetiradaDeEncomendaUseCase confirmarRetiradaDeEncomendaUseCase) {
        this.criarEncomendaUseCase = criarEncomendaUseCase;
        this.confirmarNotificacaoDeEncomendaUseCase = confirmarNotificacaoDeEncomendaUseCase;
        this.confirmarRetiradaDeEncomendaUseCase = confirmarRetiradaDeEncomendaUseCase;
    }

    @PostMapping
    public Encomenda criarEncomenda(@RequestBody EncomendaRequestDto encomendaRequestDto){
        Encomenda encomenda = EncomendaMapper.converteRequestDtoParaDomain(encomendaRequestDto);
        return criarEncomendaUseCase.execute(encomenda);
    }

    @PatchMapping("/confirma-notificacao")
    public boolean confirmaNotificacaoEncomenda(@RequestParam("idEncomenda") Long idEncomenda){
        return confirmarNotificacaoDeEncomendaUseCase.execute(idEncomenda) != null;
    }

    @PatchMapping("/retirada")
    public boolean confirmaRetiradaEncomenda(@RequestParam("idEncomenda") Long idEncomenda){
        Encomenda encomenda = new Encomenda();
        encomenda.setId(idEncomenda);
        return confirmarRetiradaDeEncomendaUseCase.execute(encomenda) != null;
    }
}
