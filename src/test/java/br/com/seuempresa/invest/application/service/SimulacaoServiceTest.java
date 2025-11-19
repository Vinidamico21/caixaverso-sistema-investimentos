package br.com.seuempresa.invest.application.service;

import br.com.caixaverso.invest.application.dto.*;
import br.com.caixaverso.invest.application.port.out.ClientePort;
import br.com.caixaverso.invest.application.port.out.ProdutoInvestimentoPort;
import br.com.caixaverso.invest.application.port.out.SimulacaoInvestimentoPort;
import br.com.caixaverso.invest.application.service.SimulacaoService;
import br.com.caixaverso.invest.domain.model.Cliente;
import br.com.caixaverso.invest.domain.model.ProdutoInvestimento;
import br.com.caixaverso.invest.domain.model.SimulacaoInvestimento;
import br.com.caixaverso.invest.infra.exception.NotFoundException;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@QuarkusTest
class SimulacaoServiceTest {

    @Inject
    SimulacaoService service;

    @InjectMock ClientePort clientePort;
    @InjectMock ProdutoInvestimentoPort produtoPort;
    @InjectMock SimulacaoInvestimentoPort simulacaoPort;

    Cliente cliente;
    ProdutoInvestimento p1;
    ProdutoInvestimento p2;
    SimularInvestimentoRequest req;

    @BeforeEach
    void setup() {
        cliente = Cliente.builder()
                .id(10L)
                .nome("João")
                .documento("123")
                .rendaMensal(new BigDecimal("5000"))
                .dataNascimento(LocalDate.of(1990, 5, 10))
                .build();

        p1 = ProdutoInvestimento.builder()
                .id(1L)
                .codigo("CDB100")
                .nome("CDB 100%")
                .tipo("RENDA_FIXA")
                .risco("BAIXO")
                .taxaAnual(new BigDecimal("0.12"))
                .liquidez("D+1")
                .prazoMinMeses(6)
                .prazoMaxMeses(24)
                .valorMinimo(new BigDecimal("100"))
                .ativo(true)
                .build();

        p2 = ProdutoInvestimento.builder()
                .id(2L)
                .codigo("TESSELIC")
                .nome("Tesouro Selic")
                .tipo("RENDA_FIXA")
                .risco("MEDIO")
                .taxaAnual(new BigDecimal("0.15"))
                .liquidez("D+0")
                .prazoMinMeses(1)
                .prazoMaxMeses(120)
                .valorMinimo(new BigDecimal("30"))
                .ativo(true)
                .build();

        req = SimularInvestimentoRequest.builder()
                .clienteId(10L)
                .tipoProduto("RENDA_FIXA")
                .valor(new BigDecimal("1000"))
                .prazoMeses(12)
                .build();
    }

    // -----------------------------------------------------
    // executarSimulacao()
    // -----------------------------------------------------
    @Test
    void deveExecutarSimulacaoComSucesso() {
        when(clientePort.findById(10L)).thenReturn(Optional.of(cliente));
        when(produtoPort.findByTipo("RENDA_FIXA")).thenReturn(List.of(p1, p2));
        when(simulacaoPort.salvar(any())).thenReturn(criarSimulacao());

        SimularInvestimentoResponse resp = service.executarSimulacao(req);

        assertNotNull(resp);
        assertNotNull(resp.getProdutoValidado());
        assertNotNull(resp.getResultadoSimulacao());

        // produto com maior taxa → p2
        assertEquals("Tesouro Selic", resp.getProdutoValidado().getNome());

        verify(clientePort).findById(10L);
        verify(produtoPort).findByTipo("RENDA_FIXA");
        verify(simulacaoPort).salvar(any());
        verifyNoMoreInteractions(clientePort, produtoPort, simulacaoPort);
    }

    @Test
    void deveFalharQuandoClienteNaoExiste() {
        when(clientePort.findById(10L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> service.executarSimulacao(req));

        verify(clientePort).findById(10L);
        verifyNoMoreInteractions(clientePort, produtoPort, simulacaoPort);
    }

    @Test
    void deveFalharQuandoNaoExistemProdutos() {
        when(clientePort.findById(10L)).thenReturn(Optional.of(cliente));
        when(produtoPort.findByTipo("RENDA_FIXA")).thenReturn(List.of());

        assertThrows(NotFoundException.class, () -> service.executarSimulacao(req));

        verify(clientePort).findById(10L);
        verify(produtoPort).findByTipo("RENDA_FIXA");
        verifyNoMoreInteractions(clientePort, produtoPort, simulacaoPort);
    }

    // -----------------------------------------------------
    // listarSimulacoes()
    // -----------------------------------------------------
    @Test
    void deveListarTodasSimulacoesQuandoClienteIdNulo() {
        SimulacaoInvestimento sim = criarSimulacao();

        when(simulacaoPort.listar()).thenReturn(List.of(sim));

        List<SimulacaoResumoDTO> lista = service.listarSimulacoes(null);

        assertEquals(1, lista.size());
        assertEquals(sim.getId(), lista.get(0).getId());

        verify(simulacaoPort).listar();
        verifyNoMoreInteractions(simulacaoPort);
    }

    @Test
    void deveListarSimulacoesPorCliente() {
        SimulacaoInvestimento sim = criarSimulacao();

        when(simulacaoPort.listarPorClienteId(10L)).thenReturn(List.of(sim));

        List<SimulacaoResumoDTO> lista = service.listarSimulacoes(10L);

        assertEquals(1, lista.size());
        assertEquals(10L, lista.get(0).getClienteId());

        verify(simulacaoPort).listarPorClienteId(10L);
        verifyNoMoreInteractions(simulacaoPort);
    }

    // -----------------------------------------------------
    // agrupamentoPorProdutoDia()
    // -----------------------------------------------------
    @Test
    void deveAgruparSimulacoesPorProdutoDia() {
        SimulacaoInvestimento sim1 = criarSimulacao();
        SimulacaoInvestimento sim2 = criarSimulacao();
        sim2.setValorFinal(new BigDecimal("1300"));

        when(simulacaoPort.listar()).thenReturn(List.of(sim1, sim2));

        List<SimulacaoPorProdutoDiaDTO> resp = service.agrupamentoPorProdutoDia();

        assertEquals(1, resp.size());
        assertEquals("Tesouro Selic", resp.get(0).getProduto());
        assertEquals(2L, resp.get(0).getQuantidadeSimulacoes());

        // Evita falha por diferença de escala (1250 vs 1250.00)
        BigDecimal esperado = new BigDecimal("1250.00");
        assertEquals(0, esperado.compareTo(resp.get(0).getMediaValorFinal()));

        verify(simulacaoPort).listar();
        verifyNoMoreInteractions(simulacaoPort);
    }

    // -----------------------------------------------------
    // helper
    // -----------------------------------------------------
    private SimulacaoInvestimento criarSimulacao() {
        return SimulacaoInvestimento.builder()
                .id(1L)
                .cliente(cliente)
                .produto(p2)
                .valorAplicado(new BigDecimal("1000"))
                .valorFinal(new BigDecimal("1200"))
                .prazoMeses(12)
                .dataSimulacao(LocalDateTime.now())
                .perfilRiscoCalculado("MEDIO")
                .build();
    }
}
