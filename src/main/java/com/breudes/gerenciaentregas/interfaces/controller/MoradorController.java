package com.breudes.gerenciaentregas.interfaces.controller;

import com.breudes.gerenciaentregas.core.domain.Morador;
import com.breudes.gerenciaentregas.core.usecases.morador.AtualizarMoradorUseCase;
import com.breudes.gerenciaentregas.core.usecases.morador.CriarMoradorUseCase;
import com.breudes.gerenciaentregas.interfaces.dto.ApiErrorDTO;
import com.breudes.gerenciaentregas.interfaces.dto.morador.MoradorOutputDTO;
import com.breudes.gerenciaentregas.interfaces.dto.morador.MoradorRequestDTO;
import com.breudes.gerenciaentregas.interfaces.mapper.MoradorMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/moradores")
@Tag(name = "Moradores", description = "Cadastro e atualização dos moradores destinatários.")
public class MoradorController {
    private final CriarMoradorUseCase criarMoradorUseCase;
    private final AtualizarMoradorUseCase atualizarMoradorUseCase;

    public MoradorController(CriarMoradorUseCase criarMoradorUseCase, AtualizarMoradorUseCase atualizarMoradorUseCase) {
        this.criarMoradorUseCase = criarMoradorUseCase;
        this.atualizarMoradorUseCase = atualizarMoradorUseCase;
    }

    @PostMapping
        @Operation(summary = "Cadastrar morador", description = "Cadastra os dados de contato e localização de um morador.")
        @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Morador cadastrado"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos ou telefone já cadastrado",
                content = @Content(schema = @Schema(implementation = ApiErrorDTO.class)))
        })
    public Morador criaMorador(@RequestBody MoradorRequestDTO moradorRequestDTO) {
        return criarMoradorUseCase.execute(MoradorMapper.converteRequestDtoParaEntity(moradorRequestDTO));
    }

    @PutMapping
        @Operation(summary = "Atualizar morador", description = "Atualiza os dados de um morador identificado pelo telefone atual.")
        @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Morador atualizado"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos, morador não encontrado ou telefone em uso",
                content = @Content(schema = @Schema(implementation = ApiErrorDTO.class)))
        })
        public Morador atualizaMorador(
            @Parameter(name = "telefoneAtual", description = "Telefone atual usado para localizar o morador", example = "11999999999", required = true)
            @RequestParam("telefoneAtual") String telefoneAtual,
            @RequestBody MoradorOutputDTO moradorOutputDTO
        ){
        return atualizarMoradorUseCase.execute(telefoneAtual, MoradorMapper.converteOutputDtoParaEntity(moradorOutputDTO));
    }
}
