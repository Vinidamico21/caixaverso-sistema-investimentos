package br.com.caixaverso.invest.application.service;

import br.com.caixaverso.invest.application.dto.PerfilRiscoResponseDTO;
import br.com.caixaverso.invest.application.dto.ProdutoRecomendadoDTO;
import br.com.caixaverso.invest.application.dto.RecomendacaoResponseDTO;
import br.com.caixaverso.invest.application.port.out.*;
import br.com.caixaverso.invest.infra.exception.BusinessException;
import br.com.caixaverso.invest.infra.exception.NotFoundException;
import br.com.caixaverso.invest.infra.persistence.entity.Cliente;
import br.com.caixaverso.invest.infra.persistence.entity.PerfilRiscoRegra;
import br.com.caixaverso.invest.infra.persistence.entity.ProdutoInvestimento;
import br.com.caixaverso.invest.infra.persistence.entity.SimulacaoInvestimento;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static br.com.caixaverso.invest.domain.constants.PerfilConstantes.PERFIL_DESCONHECIDO;
import static br.com.caixaverso.invest.domain.constants.PerfilConstantes.PERFIL_MODERADO;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MotorRecomendacaoServicePerfilTest {

    @InjectMocks
    MotorRecomendacaoService service;

    @Mock
    PerfilRiscoRegraPort perfilRegraPort;
    @Mock
    PreferenciaRegraPort preferenciaRegraPort;
    @Mock
    FrequenciaInvestRegraPort freqInvestRegraPort;
    @Mock
    FrequenciaSimulacaoRegraPort freqSimRegraPort;
    @Mock
    ProdutoRiscoRegraPort riscoRegraPort;

    @Mock
    SimulacaoInvestimentoPort simulacaoPort;
    @Mock
    InvestimentoPort investimentoPort;
    @Mock
    ProdutoInvestimentoPort produtoPort;

    Cliente cliente;
    ProdutoInvestimento ativo1;
    ProdutoInvestimento ativo2;
    ProdutoInvestimento inativo;

    @BeforeEach
    void setup() {
        cliente = Cliente.builder()
                .id(10L)
                .nome("João")
                .documento("123")
                .build();

        ativo1 = ProdutoInvestimento.builder()
                .id(1L)
                .nome("CDB 100%")
                .tipo("RENDA_FIXA")
                .liquidez("DIARIA")
                .risco("BAIXO")
                .ativo(true)
                .taxaAnual(new BigDecimal("0.12"))
                .prazoMinMeses(1)
                .build();

        ativo2 = ProdutoInvestimento.builder()
                .id(2L)
                .nome("Tesouro Selic")
                .tipo("RENDA_FIXA")
                .liquidez("D+0")
                .risco("MEDIO")
                .ativo(true)
                .taxaAnual(new BigDecimal("0.15"))
                .prazoMinMeses(2)
                .build();

        inativo = ProdutoInvestimento.builder()
                .id(3L)
                .nome("Produto Inativo")
                .tipo("RENDA_FIXA")
                .ativo(false)
                .build();
    }

    // ============================================================
    // calcularPerfil()
    // ============================================================
    @Test
    void deveFalharClienteIdInvalido() {
        assertThrows(BusinessException.class, () -> service.calcularPerfil(null));
        assertThrows(BusinessException.class, () -> service.calcularPerfil(0L));
        assertThrows(BusinessException.class, () -> service.calcularPerfil(-5L));
    }

    @Test
    void deveCalcularPerfilComSucesso() {
        SimulacaoInvestimento sim1 = sim(cliente, ativo1, "1000");
        SimulacaoInvestimento sim2 = sim(cliente, ativo2, "2000");

        when(simulacaoPort.listar()).thenReturn(List.of(sim1, sim2));
        when(preferenciaRegraPort.buscarPontuacao(anyString())).thenReturn(10);
        when(freqInvestRegraPort.buscarPontuacao(anyInt())).thenReturn(5);
        when(freqSimRegraPort.buscarPontuacao(anyInt())).thenReturn(5);
        when(investimentoPort.findByClienteIdAndPeriodo(eq(10L), any()))
                .thenReturn(List.of());

        PerfilRiscoRegra regra = new PerfilRiscoRegra();
        regra.setPerfil("MODERADO");

        when(perfilRegraPort.buscarRegraPorScore(anyInt()))
                .thenReturn(Optional.of(regra));

        PerfilRiscoResponseDTO resp = service.calcularPerfil(10L);

        assertNotNull(resp);
        assertEquals("MODERADO", resp.getPerfil());
        assertEquals("25", resp.getPontuacao());
        verify(simulacaoPort, times(2)).listar();
    }

    // ============================================================
    // recomendarParaCliente()
    // ============================================================
    @Test
    void deveRecomendarParaClienteComSucesso() {
        SimulacaoInvestimento sim = sim(cliente, ativo1, "1500");

        when(simulacaoPort.listar()).thenReturn(List.of(sim));
        when(preferenciaRegraPort.buscarPontuacao(anyString())).thenReturn(5);
        when(freqInvestRegraPort.buscarPontuacao(anyInt())).thenReturn(5);
        when(freqSimRegraPort.buscarPontuacao(anyInt())).thenReturn(5);
        when(investimentoPort.findByClienteIdAndPeriodo(eq(10L), any()))
                .thenReturn(List.of());

        PerfilRiscoRegra regra = new PerfilRiscoRegra();
        regra.setPerfil("MODERADO");

        when(perfilRegraPort.buscarRegraPorScore(anyInt()))
                .thenReturn(Optional.of(regra));

        when(produtoPort.findAll()).thenReturn(List.of(ativo1, ativo2));
        when(riscoRegraPort.classificar(any())).thenReturn("MEDIO");

        RecomendacaoResponseDTO resp = service.recomendarParaCliente(10L);

        assertNotNull(resp);
        assertEquals("MODERADO", resp.getPerfilRisco());
        assertTrue(resp.getProdutos().size() > 0);
    }

    @Test
    void deveRetornarPerfilDesconhecidoQuandoLancarNotFound() {
        when(simulacaoPort.listar()).thenThrow(new NotFoundException("x"));

        RecomendacaoResponseDTO resp = service.recomendarParaCliente(10L);

        assertEquals(PERFIL_DESCONHECIDO, resp.getPerfilRisco());
        assertEquals(0, resp.getProdutos().size());
    }

    // ============================================================
    // recomendarPorPerfil()
    // ============================================================
    @Test
    void recomendarPorPerfilNullViraConservador() {
        when(produtoPort.findAll()).thenReturn(List.of(ativo1));
        when(riscoRegraPort.classificar(any())).thenReturn("BAIXO");

        List<ProdutoRecomendadoDTO> resp = service.recomendarPorPerfil(null);

        assertNotNull(resp);
        assertEquals(0, resp.size());
    }

    @Test
    void recomendarPorPerfilFuncionaComStringsDiversas() {
        when(produtoPort.findAll()).thenReturn(List.of(ativo1));
        when(riscoRegraPort.classificar(any())).thenReturn("BAIXO");

        List<ProdutoRecomendadoDTO> resp = service.recomendarPorPerfil("moderado");

        assertNotNull(resp);
    }

    // ============================================================
    // Pipeline de recomendação
    // ============================================================
    @Test
    void deveExecutarPipelineCompleto() {
        when(produtoPort.findAll()).thenReturn(List.of(ativo1, ativo2, inativo));

        when(riscoRegraPort.classificar(any()))
                .thenReturn("BAIXO")
                .thenReturn("MEDIO");

        List<ProdutoRecomendadoDTO> resp =
                service.recomendarPorPerfil(PERFIL_MODERADO);

        assertNotNull(resp);
        assertTrue(resp.size() > 0);
        assertTrue(resp.stream().noneMatch(r -> r.getNome().equals("Produto Inativo")));

        verify(riscoRegraPort, times(2)).classificar(any());
    }

    // ============================================================
    // Helpers
    // ============================================================
    private SimulacaoInvestimento sim(Cliente c, ProdutoInvestimento p, String valor) {
        return SimulacaoInvestimento.builder()
                .id(1L)
                .cliente(c)
                .produto(p)
                .valorAplicado(new BigDecimal(valor))
                .valorFinal(new BigDecimal(valor).add(BigDecimal.TEN))
                .dataSimulacao(LocalDateTime.now())
                .prazoMeses(12)
                .build();
    }
}
