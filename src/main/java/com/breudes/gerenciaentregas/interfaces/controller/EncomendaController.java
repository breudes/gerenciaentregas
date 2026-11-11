package com.breudes.gerenciaentregas.interfaces.controller;

import com.breudes.gerenciaentregas.core.domain.Encomenda;
import com.breudes.gerenciaentregas.core.usecases.encomenda.ConfirmarNotificacaoDeEncomendaUseCase;
import com.breudes.gerenciaentregas.core.usecases.encomenda.ConfirmarRetiradaDeEncomendaUseCase;
import com.breudes.gerenciaentregas.core.usecases.encomenda.CriarEncomendaUseCase;
import com.breudes.gerenciaentregas.interfaces.dto.ApiErrorDTO;
import com.breudes.gerenciaentregas.interfaces.dto.encomenda.EncomendaRequestDto;
import com.breudes.gerenciaentregas.interfaces.mapper.EncomendaMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/encomendas")
@Tag(name = "Encomendas", description = "Cadastro e acompanhamento de encomendas recebidas.")
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
        @Operation(summary = "Cadastrar encomenda", description = "Cadastra uma encomenda e solicita uma notificação ao destinatário.")
        @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Encomenda cadastrada"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos",
                content = @Content(schema = @Schema(implementation = ApiErrorDTO.class)))
        })
    public Encomenda criarEncomenda(@RequestBody EncomendaRequestDto encomendaRequestDto){
        Encomenda encomenda = EncomendaMapper.converteRequestDtoParaDomain(encomendaRequestDto);
        return criarEncomendaUseCase.execute(encomenda);
    }

    @PatchMapping("/confirma-notificacao")
        @Operation(summary = "Confirmar notificação", description = "Registra que a notificação da encomenda foi recebida.")
        @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Indica se a confirmação foi realizada"),
            @ApiResponse(responseCode = "400", description = "ID inválido ou encomenda não encontrada",
                content = @Content(schema = @Schema(implementation = ApiErrorDTO.class)))
        })
        public boolean confirmaNotificacaoEncomenda(
            @Parameter(name = "idEncomenda", description = "Identificador da encomenda", example = "42", required = true)
            @RequestParam("idEncomenda") Long idEncomenda
        ){
        return confirmarNotificacaoDeEncomendaUseCase.execute(idEncomenda) != null;
    }

    @PatchMapping("/retirada")
        @Operation(summary = "Confirmar retirada", description = "Confirma a retirada de uma encomenda previamente notificada.")
        @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Indica se a retirada foi confirmada"),
            @ApiResponse(responseCode = "400", description = "Retirada inválida ou encomenda não encontrada",
                content = @Content(schema = @Schema(implementation = ApiErrorDTO.class)))
        })
        public boolean confirmaRetiradaEncomenda(
            @Parameter(name = "idEncomenda", description = "Identificador da encomenda", example = "42", required = true)
            @RequestParam("idEncomenda") Long idEncomenda
        ){
        Encomenda encomenda = new Encomenda();
        encomenda.setId(idEncomenda);
        return confirmarRetiradaDeEncomendaUseCase.execute(encomenda) != null;
    }
}
