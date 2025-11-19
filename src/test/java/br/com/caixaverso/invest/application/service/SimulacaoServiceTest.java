package br.com.caixaverso.invest.application.service;

import br.com.caixaverso.invest.application.dto.PageResponse;
import br.com.caixaverso.invest.application.dto.SimularInvestimentoRequest;
import br.com.caixaverso.invest.application.dto.SimularInvestimentoResponse;
import br.com.caixaverso.invest.application.dto.SimulacaoPorProdutoDiaDTO;
import br.com.caixaverso.invest.application.dto.SimulacaoResumoDTO;
import br.com.caixaverso.invest.application.port.out.ClientePort;
import br.com.caixaverso.invest.application.port.out.ProdutoInvestimentoPort;
import br.com.caixaverso.invest.application.port.out.SimulacaoInvestimentoPort;
import br.com.caixaverso.invest.domain.model.Cliente;
import br.com.caixaverso.invest.domain.model.ProdutoInvestimento;
import br.com.caixaverso.invest.domain.model.SimulacaoInvestimento;
import br.com.caixaverso.invest.infra.exception.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SimulacaoServiceTest {

    @InjectMocks
    SimulacaoService service;

    @Mock ClientePort clientePort;
    @Mock ProdutoInvestimentoPort produtoPort;
    @Mock SimulacaoInvestimentoPort simulacaoPort;

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
        // arrange
        SimulacaoInvestimento sim = criarSimulacao();

        when(simulacaoPort.listar()).thenReturn(List.of(sim));

        int page = 0;
        int size = 10;

        // act
        PageResponse<SimulacaoResumoDTO> pagina = service.listarSimulacoes(null, page, size);

        // assert
        assertNotNull(pagina);
        assertEquals(page, pagina.getPage());
        assertEquals(size, pagina.getSize());
        assertEquals(1, pagina.getTotalElements());
        assertEquals(1, pagina.getTotalPages()); // ceil(1 / 10) = 1
        assertEquals(1, pagina.getContent().size());

        SimulacaoResumoDTO dto = pagina.getContent().get(0);
        assertEquals(sim.getId(), dto.getId());
        assertEquals(sim.getCliente().getId(), dto.getClienteId());
        assertEquals(sim.getProduto().getNome(), dto.getProduto());
        assertEquals(sim.getValorAplicado(), dto.getValorInvestido());
        assertEquals(sim.getValorFinal(), dto.getValorFinal());
        assertEquals(sim.getPrazoMeses(), dto.getPrazoMeses());

        verify(simulacaoPort).listar();
        verifyNoMoreInteractions(simulacaoPort);
    }

    @Test
    void deveListarSimulacoesPorCliente() {
        // arrange
        SimulacaoInvestimento sim = criarSimulacao();
        Long clienteId = 10L;
        sim.getCliente().setId(clienteId); // garante consistência no DTO

        when(simulacaoPort.listarPorClienteId(clienteId)).thenReturn(List.of(sim));

        int page = 0;
        int size = 10;

        // act
        PageResponse<SimulacaoResumoDTO> pagina = service.listarSimulacoes(clienteId, page, size);

        // assert
        assertNotNull(pagina);
        assertEquals(page, pagina.getPage());
        assertEquals(size, pagina.getSize());
        assertEquals(1, pagina.getTotalElements());
        assertEquals(1, pagina.getTotalPages());
        assertEquals(1, pagina.getContent().size());

        SimulacaoResumoDTO dto = pagina.getContent().get(0);
        assertEquals(clienteId, dto.getClienteId());
        assertEquals(sim.getId(), dto.getId());
        assertEquals(sim.getProduto().getNome(), dto.getProduto());
        assertEquals(sim.getValorAplicado(), dto.getValorInvestido());
        assertEquals(sim.getValorFinal(), dto.getValorFinal());

        verify(simulacaoPort).listarPorClienteId(clienteId);
        verifyNoMoreInteractions(simulacaoPort);
    }


    // -----------------------------------------------------
    // agrupamentoPorProdutoDia() COM paginação em memória
    // -----------------------------------------------------
    @Test
    void deveAgruparSimulacoesPorProdutoDiaComPaginacao() {
        SimulacaoInvestimento sim1 = criarSimulacao();
        SimulacaoInvestimento sim2 = criarSimulacao();
        sim2.setValorFinal(new BigDecimal("1300"));

        when(simulacaoPort.listar()).thenReturn(List.of(sim1, sim2));

        int page = 0;
        int size = 10;

        PageResponse<SimulacaoPorProdutoDiaDTO> resp =
                service.agrupamentoPorProdutoDia(page, size);

        assertNotNull(resp);
        assertEquals(page, resp.getPage());
        assertEquals(size, resp.getSize());
        assertEquals(1, resp.getContent().size());

        SimulacaoPorProdutoDiaDTO dto = resp.getContent().get(0);

        assertEquals("Tesouro Selic", dto.getProduto());
        assertEquals(2L, dto.getQuantidadeSimulacoes());

        // média = (1200 + 1300) / 2 = 1250.00
        BigDecimal esperado = new BigDecimal("1250.00");
        assertEquals(0, esperado.compareTo(dto.getMediaValorFinal()));

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
