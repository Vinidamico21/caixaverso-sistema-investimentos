package br.com.caixaverso.invest.application.service;

import br.com.caixaverso.invest.application.dto.InvestimentoResponseDTO;
import br.com.caixaverso.invest.application.dto.response.PageResponse;
import br.com.caixaverso.invest.infra.persistence.entity.Investimento;
import br.com.caixaverso.invest.infra.persistence.entity.ProdutoInvestimento;
import br.com.caixaverso.invest.infra.repository.InvestimentoRepository;
import io.quarkus.panache.common.Page;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InvestimentoServiceTest {

    @Mock
    InvestimentoRepository investimentoRepository;

    @Mock
    PanacheQuery<Investimento> panacheQuery;

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
    // TESTE 1 - Página contendo investimentos
    // ---------------------------------------------------------------------------
    @Test
    void testListarPorCliente_ComInvestimentos() {

        when(investimentoRepository.find(
                "cliente.id = ?1 order by dataAporte desc", 5L))
                .thenReturn(panacheQuery);

        when(panacheQuery.count()).thenReturn(1L);
        when(panacheQuery.page(any(Page.class))).thenReturn(panacheQuery);
        when(panacheQuery.list()).thenReturn(List.of(investimento));

        PageResponse<InvestimentoResponseDTO> page =
                investimentoService.listarPorCliente("5", 0, 10);

        assertNotNull(page);
        assertEquals(1L, page.getTotalElements());
        assertEquals(1, page.getTotalPages());
        assertEquals(0, page.getPage());
        assertEquals(10, page.getSize());
        assertEquals(1, page.getContent().size());

        InvestimentoResponseDTO dto = page.getContent().get(0);

        assertEquals(10L, dto.getId());
        assertEquals("CDB", dto.getTipo());
        assertEquals(BigDecimal.valueOf(1500), dto.getValor());
        assertEquals(BigDecimal.valueOf(0.12), dto.getRentabilidade());
        assertEquals(LocalDate.parse("2024-10-10"), dto.getData());

        verify(investimentoRepository, times(1))
                .find("cliente.id = ?1 order by dataAporte desc", 5L);
        verify(panacheQuery, times(1)).count();
        verify(panacheQuery, times(1)).page(any(Page.class));
        verify(panacheQuery, times(1)).list();
        verifyNoMoreInteractions(investimentoRepository, panacheQuery);
    }

    // ---------------------------------------------------------------------------
    // TESTE 2 - Nenhum investimento encontrado
    // ---------------------------------------------------------------------------
    @Test
    void testListarPorCliente_Vazio() {

        when(investimentoRepository.find(
                "cliente.id = ?1 order by dataAporte desc", 8L))
                .thenReturn(panacheQuery);

        when(panacheQuery.count()).thenReturn(0L);
        when(panacheQuery.page(any(Page.class))).thenReturn(panacheQuery);
        when(panacheQuery.list()).thenReturn(List.of());

        PageResponse<InvestimentoResponseDTO> page =
                investimentoService.listarPorCliente("8", 0, 10);

        assertNotNull(page);
        assertTrue(page.getContent().isEmpty());
        assertEquals(0L, page.getTotalElements());
        assertEquals(0, page.getTotalPages());

        verify(investimentoRepository).find(
                "cliente.id = ?1 order by dataAporte desc", 8L
        );
        verify(panacheQuery, times(1)).count();
        verify(panacheQuery, times(1)).page(any(Page.class));
        verify(panacheQuery, times(1)).list();
        verifyNoMoreInteractions(investimentoRepository, panacheQuery);
    }

    // ---------------------------------------------------------------------------
    // TESTE 3 - Produto nulo
    // ---------------------------------------------------------------------------
    @Test
    void testListarPorCliente_ProdutoNulo() {

        investimento.setProduto(null);

        when(investimentoRepository.find(
                "cliente.id = ?1 order by dataAporte desc", 3L))
                .thenReturn(panacheQuery);

        when(panacheQuery.count()).thenReturn(1L);
        when(panacheQuery.page(any(Page.class))).thenReturn(panacheQuery);
        when(panacheQuery.list()).thenReturn(List.of(investimento));

        PageResponse<InvestimentoResponseDTO> page =
                investimentoService.listarPorCliente("3", 0, 10);

        InvestimentoResponseDTO dto = page.getContent().get(0);

        assertNull(dto.getTipo());
        assertNull(dto.getRentabilidade());
    }

    // ---------------------------------------------------------------------------
    // TESTE 4 - Data nula
    // ---------------------------------------------------------------------------
    @Test
    void testListarPorCliente_DataNula() {

        investimento.setDataAporte(null);

        when(investimentoRepository.find(
                "cliente.id = ?1 order by dataAporte desc", 4L))
                .thenReturn(panacheQuery);

        when(panacheQuery.count()).thenReturn(1L);
        when(panacheQuery.page(any(Page.class))).thenReturn(panacheQuery);
        when(panacheQuery.list()).thenReturn(List.of(investimento));

        PageResponse<InvestimentoResponseDTO> page =
                investimentoService.listarPorCliente("4", 0, 10);

        InvestimentoResponseDTO dto = page.getContent().get(0);

        assertNull(dto.getData());
    }
}
