package br.com.seuempresa.invest.application.service;

import br.com.caixaverso.invest.application.dto.InvestimentoResponseDTO;
import br.com.caixaverso.invest.application.service.InvestimentoService;
import br.com.caixaverso.invest.domain.model.Investimento;
import br.com.caixaverso.invest.domain.model.ProdutoInvestimento;
import br.com.caixaverso.invest.infra.repository.InvestimentoRepository;
import org.jboss.logging.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InvestimentoServiceTest {

    @Mock
    InvestimentoRepository investimentoRepository;

    @InjectMocks
    InvestimentoService investimentoService;

    private Investimento investimento;
    private ProdutoInvestimento produto;

    @BeforeEach
    void setup() {
        produto = new ProdutoInvestimento();
        produto.setTipo("CDB");
        produto.setTaxaAnual(BigDecimal.valueOf(0.12));

        investimento = new Investimento();
        investimento.setId(10L);
        investimento.setProduto(produto);
        investimento.setValorAplicado(BigDecimal.valueOf(1500));
        investimento.setDataAporte(OffsetDateTime.parse("2024-10-10T10:00:00Z").toLocalDateTime());
    }

    // ---------------------------------------------------------------------------
    // TESTE 1 - Lista contendo investimentos
    // ---------------------------------------------------------------------------

    @Test
    void testListarPorCliente_ComInvestimentos() {

        when(investimentoRepository.list(
                "cliente.id = ?1 order by dataAporte desc", 5L))
                .thenReturn(List.of(investimento));

        List<InvestimentoResponseDTO> result =
                investimentoService.listarPorCliente("5");

        assertNotNull(result);
        assertEquals(1, result.size());

        InvestimentoResponseDTO dto = result.get(0);

        assertEquals(10L, dto.getId());
        assertEquals("CDB", dto.getTipo());
        assertEquals(BigDecimal.valueOf(1500), dto.getValor());
        assertEquals(BigDecimal.valueOf(0.12), dto.getRentabilidade());
        assertEquals(LocalDate.parse("2024-10-10"), dto.getData());

        verify(investimentoRepository, times(1))
                .list("cliente.id = ?1 order by dataAporte desc", 5L);
    }

    // ---------------------------------------------------------------------------
    // TESTE 2 - Nenhum investimento encontrado
    // ---------------------------------------------------------------------------

    @Test
    void testListarPorCliente_Vazio() {

        when(investimentoRepository.list(
                "cliente.id = ?1 order by dataAporte desc", 8L))
                .thenReturn(List.of());

        List<InvestimentoResponseDTO> result =
                investimentoService.listarPorCliente("8");

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(investimentoRepository).list(
                "cliente.id = ?1 order by dataAporte desc", 8L
        );
    }

    // ---------------------------------------------------------------------------
    // TESTE 3 - Produto nulo
    // ---------------------------------------------------------------------------

    @Test
    void testListarPorCliente_ProdutoNulo() {

        investimento.setProduto(null);

        when(investimentoRepository.list(
                "cliente.id = ?1 order by dataAporte desc", 3L))
                .thenReturn(List.of(investimento));

        List<InvestimentoResponseDTO> result =
                investimentoService.listarPorCliente("3");

        InvestimentoResponseDTO dto = result.get(0);

        assertNull(dto.getTipo());
        assertNull(dto.getRentabilidade());
    }

    // ---------------------------------------------------------------------------
    // TESTE 4 - Data nula
    // ---------------------------------------------------------------------------

    @Test
    void testListarPorCliente_DataNula() {

        investimento.setDataAporte(null);

        when(investimentoRepository.list(
                "cliente.id = ?1 order by dataAporte desc", 4L))
                .thenReturn(List.of(investimento));

        List<InvestimentoResponseDTO> result =
                investimentoService.listarPorCliente("4");

        InvestimentoResponseDTO dto = result.get(0);

        assertNull(dto.getData());
    }
}

