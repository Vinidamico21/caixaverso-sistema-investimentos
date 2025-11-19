package br.com.seuempresa.invest.application.dto;

import br.com.caixaverso.invest.application.dto.ProdutoRecomendadoDTO;
import br.com.caixaverso.invest.application.dto.RecomendacaoResponseDTO;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RecomendacaoResponseDTOTest {

    @Test
    void testNoArgsConstructor() {
        RecomendacaoResponseDTO dto = new RecomendacaoResponseDTO();

        assertNotNull(dto);
        assertNull(dto.getClienteId());
        assertNull(dto.getPerfilRisco());
        assertNull(dto.getScoreRisco());
        assertNull(dto.getProdutos());
    }

    @Test
    void testAllArgsConstructor() {
        ProdutoRecomendadoDTO p1 = new ProdutoRecomendadoDTO(
                1L, "CDB XP", "Renda Fixa",
                new BigDecimal("0.10"), "Baixo"
        );

        ProdutoRecomendadoDTO p2 = new ProdutoRecomendadoDTO(
                2L, "FII AAA11", "Fundo Imobiliário",
                new BigDecimal("0.09"), "Médio"
        );

        List<ProdutoRecomendadoDTO> produtos = List.of(p1, p2);

        RecomendacaoResponseDTO dto = new RecomendacaoResponseDTO(
                10L, "Moderado", "75", produtos
        );

        assertEquals(10L, dto.getClienteId());
        assertEquals("Moderado", dto.getPerfilRisco());
        assertEquals("75", dto.getScoreRisco());
        assertEquals(produtos, dto.getProdutos());
    }

    @Test
    void testToString() {
        ProdutoRecomendadoDTO p = ProdutoRecomendadoDTO.builder()
                .id(9L)
                .nome("LCI Bank")
                .tipo("Renda Fixa")
                .rentabilidade(new BigDecimal("0.08"))
                .risco("Baixo")
                .build();

        RecomendacaoResponseDTO dto = RecomendacaoResponseDTO.builder()
                .clienteId(50L)
                .perfilRisco("Moderado")
                .scoreRisco("65")
                .produtos(List.of(p))
                .build();

        String str = dto.toString();

        assertTrue(str.contains("50"));
        assertTrue(str.contains("Moderado"));
        assertTrue(str.contains("65"));
        assertTrue(str.contains("LCI Bank"));
    }

    @Test
    void testSettersAndGetters() {
        RecomendacaoResponseDTO dto = new RecomendacaoResponseDTO();

        ProdutoRecomendadoDTO p = new ProdutoRecomendadoDTO(
                3L, "ETF AAA11", "ETF",
                new BigDecimal("0.15"), "Alto"
        );

        dto.setClienteId(7L);
        dto.setPerfilRisco("Conservador");
        dto.setScoreRisco("35");
        dto.setProdutos(List.of(p));

        assertEquals(7L, dto.getClienteId());
        assertEquals("Conservador", dto.getPerfilRisco());
        assertEquals("35", dto.getScoreRisco());
        assertEquals(List.of(p), dto.getProdutos());
    }

    @Test
    void testEqualsAndHashCode() {
        ProdutoRecomendadoDTO p = new ProdutoRecomendadoDTO(
                1L, "CDB XPTO", "RF",
                new BigDecimal("0.11"), "Baixo"
        );

        List<ProdutoRecomendadoDTO> lista = List.of(p);

        RecomendacaoResponseDTO dto1 = new RecomendacaoResponseDTO(
                1L, "Moderado", "70", lista
        );

        RecomendacaoResponseDTO dto2 = new RecomendacaoResponseDTO(
                1L, "Moderado", "70", lista
        );

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }
}