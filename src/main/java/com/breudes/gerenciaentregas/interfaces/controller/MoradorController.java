package com.breudes.gerenciaentregas.interfaces.controller;

import com.breudes.gerenciaentregas.core.domain.Morador;
import com.breudes.gerenciaentregas.core.usecases.morador.AtualizarMoradorUseCase;
import com.breudes.gerenciaentregas.core.usecases.morador.CriarMoradorUseCase;
import com.breudes.gerenciaentregas.interfaces.dto.MoradorOutputDTO;
import com.breudes.gerenciaentregas.interfaces.dto.MoradorRequestDTO;
import com.breudes.gerenciaentregas.interfaces.mapper.MoradorMapper;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/moradores")
public class MoradorController {
    private final CriarMoradorUseCase criarMoradorUseCase;
    private final AtualizarMoradorUseCase atualizarMoradorUseCase;

    public MoradorController(CriarMoradorUseCase criarMoradorUseCase, AtualizarMoradorUseCase atualizarMoradorUseCase) {
        this.criarMoradorUseCase = criarMoradorUseCase;
        this.atualizarMoradorUseCase = atualizarMoradorUseCase;
    }

    @PostMapping
    public Morador criaMorador(@RequestBody MoradorRequestDTO moradorRequestDTO) {
        return criarMoradorUseCase.execute(MoradorMapper.converteRequestDtoParaEntity(moradorRequestDTO));
    }

    @PutMapping
    public Morador atualizaMorador(@RequestBody MoradorOutputDTO moradorOutputDTO){
        return atualizarMoradorUseCase.execute(MoradorMapper.converteOutputDtoParaEntity(moradorOutputDTO));
    }
}
