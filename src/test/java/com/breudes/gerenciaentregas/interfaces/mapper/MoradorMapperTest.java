package com.breudes.gerenciaentregas.interfaces.mapper;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.breudes.gerenciaentregas.core.domain.Morador;
import com.breudes.gerenciaentregas.infrastructures.entity.MoradorJpa;
import com.breudes.gerenciaentregas.interfaces.dto.morador.MoradorOutputDTO;
import com.breudes.gerenciaentregas.interfaces.dto.morador.MoradorRequestDTO;
import org.junit.jupiter.api.Test;

class MoradorMapperTest {

    @Test
    void deveConverterMoradorEmJpa() {
        Morador morador = new Morador(10L, "Ana", "11998887766", "202", "B");

        MoradorJpa resultado = MoradorMapper.converteEntityparaJpa(morador);

        assertAll(
                () -> assertEquals(10L, resultado.getId()),
                () -> assertEquals("Ana", resultado.getNome()),
                () -> assertEquals("11998887766", resultado.getTelefone()),
                () -> assertEquals("202", resultado.getApartamento()),
                () -> assertEquals("B", resultado.getBloco())
        );
    }

    @Test
    void deveConverterJpaEmMorador() {
        MoradorJpa moradorJpa = new MoradorJpa(11L, "Bruno", "11997776655", "303", "C");

        Morador resultado = MoradorMapper.converteJpaParaEntity(moradorJpa);

        assertAll(
                () -> assertEquals(11L, resultado.getId()),
                () -> assertEquals("Bruno", resultado.getNome()),
                () -> assertEquals("11997776655", resultado.getTelefone()),
                () -> assertEquals("303", resultado.getApartamento()),
                () -> assertEquals("C", resultado.getBloco())
        );
    }

    @Test
    void deveConverterRequestDtoEmMorador() {
        MoradorRequestDTO dto = new MoradorRequestDTO("Carlos", "11990001122", "404", "D");

        Morador resultado = MoradorMapper.converteRequestDtoParaEntity(dto);

        assertAll(
                () -> assertEquals("Carlos", resultado.getNome()),
                () -> assertEquals("11990001122", resultado.getTelefone()),
                () -> assertEquals("404", resultado.getApartamento()),
                () -> assertEquals("D", resultado.getBloco())
        );
    }

    @Test
    void deveConverterOutputDtoEmMorador() {
        MoradorOutputDTO dto = new MoradorOutputDTO("Daniela", "11990001133", "505", "E");

        Morador resultado = MoradorMapper.converteOutputDtoParaEntity(dto);

        assertAll(
                () -> assertEquals("Daniela", resultado.getNome()),
                () -> assertEquals("11990001133", resultado.getTelefone()),
                () -> assertEquals("505", resultado.getApartamento()),
                () -> assertEquals("E", resultado.getBloco())
        );
    }

    @Test
    void deveRetornarNullQuandoParametroForNulo() {
        assertAll(
                () -> assertNull(MoradorMapper.converteEntityparaJpa(null)),
                () -> assertNull(MoradorMapper.converteJpaParaEntity(null)),
                () -> assertNull(MoradorMapper.converteRequestDtoParaEntity(null)),
                () -> assertNull(MoradorMapper.converteOutputDtoParaEntity(null))
        );
    }
}
