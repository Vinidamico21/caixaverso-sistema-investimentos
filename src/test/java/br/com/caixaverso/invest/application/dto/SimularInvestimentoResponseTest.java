package br.com.caixaverso.invest.application.dto;

import br.com.caixaverso.invest.application.dto.response.SimularInvestimentoResponse;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.*;

class SimularInvestimentoResponseTest {

    @Test
    void testNoArgsConstructor() {
        SimularInvestimentoResponse dto = new SimularInvestimentoResponse();

        assertNotNull(dto);
        assertNull(dto.getProdutoValidado());
        assertNull(dto.getResultadoSimulacao());
        assertNull(dto.getDataSimulacao());
    }

    @Test
    void testAllArgsConstructor() {
        ProdutoValidadoDTO produto = new ProdutoValidadoDTO(
                1L, "CDB XP", "Renda Fixa",
                new BigDecimal("0.10"), "Baixo"
        );

        ResultadoSimulacaoDTO resultado = new ResultadoSimulacaoDTO(
                new BigDecimal("1100"),
                new BigDecimal("0.10"),
                12
        );

        OffsetDateTime data = OffsetDateTime.now(ZoneOffset.UTC);

        SimularInvestimentoResponse dto = new SimularInvestimentoResponse(
                produto,
                resultado,
                data
        );

        assertEquals(produto, dto.getProdutoValidado());
        assertEquals(resultado, dto.getResultadoSimulacao());
        assertEquals(data, dto.getDataSimulacao());
    }

    @Test
    void testBuilder() {
        ProdutoValidadoDTO produto = ProdutoValidadoDTO.builder()
                .id(10L)
                .nome("Tesouro Selic")
                .tipo("Renda Fixa")
                .rentabilidade(new BigDecimal("0.09"))
                .risco("Baixo")
                .build();

        ResultadoSimulacaoDTO resultado = ResultadoSimulacaoDTO.builder()
                .valorFinal(new BigDecimal("1500"))
                .rentabilidadeEfetiva(new BigDecimal("0.12"))
                .prazoMeses(6)
                .build();

        OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);

        SimularInvestimentoResponse dto = SimularInvestimentoResponse.builder()
                .produtoValidado(produto)
                .resultadoSimulacao(resultado)
                .dataSimulacao(now)
                .build();

        assertEquals(produto, dto.getProdutoValidado());
        assertEquals(resultado, dto.getResultadoSimulacao());
        assertEquals(now, dto.getDataSimulacao());
    }

    @Test
    void testGettersAndSetters() {
        SimularInvestimentoResponse dto = new SimularInvestimentoResponse();

        ProdutoValidadoDTO produto = new ProdutoValidadoDTO(
                3L, "LCI Banco X", "Renda Fixa",
                new BigDecimal("0.08"), "Baixo"
        );

        ResultadoSimulacaoDTO resultado = new ResultadoSimulacaoDTO(
                new BigDecimal("3000"),
                new BigDecimal("0.15"),
                18
        );

        OffsetDateTime date = OffsetDateTime.of(
                2026, 3, 20, 14, 0, 0, 0, ZoneOffset.UTC
        );

        dto.setProdutoValidado(produto);
        dto.setResultadoSimulacao(resultado);
        dto.setDataSimulacao(date);

        assertEquals(produto, dto.getProdutoValidado());
        assertEquals(resultado, dto.getResultadoSimulacao());
        assertEquals(date, dto.getDataSimulacao());
    }

    @Test
    void testEqualsAndHashCode() {
        ProdutoValidadoDTO produto = new ProdutoValidadoDTO(
                1L, "Produto", "Tipo",
                new BigDecimal("0.10"), "Médio"
        );

        ResultadoSimulacaoDTO resultado = new ResultadoSimulacaoDTO(
                new BigDecimal("2000"),
                new BigDecimal("0.10"),
                12
        );

        OffsetDateTime date = OffsetDateTime.now(ZoneOffset.UTC);

        SimularInvestimentoResponse dto1 = new SimularInvestimentoResponse(
                produto, resultado, date
        );

        SimularInvestimentoResponse dto2 = new SimularInvestimentoResponse(
                produto, resultado, date
        );

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        ProdutoValidadoDTO produto = ProdutoValidadoDTO.builder()
                .id(999L)
                .nome("ETF XPTO11")
                .tipo("ETF")
                .rentabilidade(new BigDecimal("0.22"))
                .risco("Alto")
                .build();

        ResultadoSimulacaoDTO resultado = ResultadoSimulacaoDTO.builder()
                .valorFinal(new BigDecimal("5000"))
                .rentabilidadeEfetiva(new BigDecimal("0.18"))
                .prazoMeses(36)
                .build();

        OffsetDateTime date = OffsetDateTime.of(
                2027, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC
        );

        SimularInvestimentoResponse dto = SimularInvestimentoResponse.builder()
                .produtoValidado(produto)
                .resultadoSimulacao(resultado)
                .dataSimulacao(date)
                .build();

        String str = dto.toString();

        assertTrue(str.contains("ETF XPTO11"));
        assertTrue(str.contains("5000"));
        assertTrue(str.contains("0.18"));
        assertTrue(str.contains("36"));
        assertTrue(str.contains("2027-01-01T00:00"));
    }

    @Test
    void testBuilderToString() {
        ProdutoValidadoDTO produto = ProdutoValidadoDTO.builder()
                .id(111L)
                .nome("CDB CDI 110")
                .tipo("Renda Fixa")
                .rentabilidade(new BigDecimal("0.11"))
                .risco("Médio")
                .build();

        ResultadoSimulacaoDTO resultado = ResultadoSimulacaoDTO.builder()
                .valorFinal(new BigDecimal("2500"))
                .rentabilidadeEfetiva(new BigDecimal("0.13"))
                .prazoMeses(24)
                .build();

        OffsetDateTime date = OffsetDateTime.of(
                2028, 5, 10, 10, 30, 0, 0, ZoneOffset.UTC
        );

        SimularInvestimentoResponse.SimularInvestimentoResponseBuilder builder =
                SimularInvestimentoResponse.builder()
                        .produtoValidado(produto)
                        .resultadoSimulacao(resultado)
                        .dataSimulacao(date);

        String builderStr = builder.toString();

        assertNotNull(builderStr);
        assertTrue(builderStr.contains("produtoValidado"));
        assertTrue(builderStr.contains("resultadoSimulacao"));
        assertTrue(builderStr.contains("dataSimulacao"));
    }
}
