package br.com.seuempresa.invest.infra.adapter;

import br.com.caixaverso.invest.domain.model.Cliente;
import br.com.caixaverso.invest.domain.model.ProdutoInvestimento;
import br.com.caixaverso.invest.domain.model.SimulacaoInvestimento;
import br.com.caixaverso.invest.infra.adapter.SimulacaoInvestimentoAdapter;
import br.com.caixaverso.invest.infra.repository.SimulacaoInvestimentoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

/**
 * Testes unitários para o {@link SimulacaoInvestimentoAdapter}.
 * Verifica se o adaptador interage corretamente com o repositório de simulações.
 */
@ExtendWith(MockitoExtension.class)
class SimulacaoInvestimentoAdapterTest {

    @Mock
    private SimulacaoInvestimentoRepository simulacaoRepository;

    @InjectMocks
    private SimulacaoInvestimentoAdapter simulacaoInvestimentoAdapter;

    private SimulacaoInvestimento simulacao;
    private List<SimulacaoInvestimento> simulacoes;
    private Long clienteId;

    /**
     * Método executado antes de cada teste para inicializar os OBJETOS DE TESTE.
     * NENHUMA CONFIGURAÇÃO DE MOCK (when/thenReturn) deve ficar aqui.
     */
    @BeforeEach
    void setUp() {
        clienteId = 123L;

        // Criando mocks simples
        Cliente clienteMock = mock(Cliente.class);
        ProdutoInvestimento produtoMock = mock(ProdutoInvestimento.class);

        simulacao = SimulacaoInvestimento.builder()
                .id(1L)
                .cliente(clienteMock)
                .produto(produtoMock)
                .valorAplicado(new BigDecimal("1000.00"))
                .prazoMeses(12)
                .valorFinal(new BigDecimal("1100.00"))
                .dataSimulacao(LocalDateTime.now())
                .build();

        simulacoes = List.of(simulacao);
    }

    @Test
    @DisplayName("Deve salvar uma simulação chamando o método persist do repositório")
    void deveSalvarSimulacao() {
        // Ação (Act)
        simulacaoInvestimentoAdapter.salvar(simulacao);

        // Verificação (Assert)
        // Verifica se o método `persist` do repositório foi chamado com o objeto correto
        verify(simulacaoRepository, times(1)).persist(simulacao);
    }

    @Test
    @DisplayName("Deve listar todas as simulações chamando o método listAll do repositório")
    void deveListarTodasSimulacoes() {
        // Organização (Arrange) - O STUB FICA AQUI, dentro do teste que o usa
        when(simulacaoRepository.listAll()).thenReturn(simulacoes);

        // Ação (Act)
        List<SimulacaoInvestimento> resultado = simulacaoInvestimentoAdapter.listar();

        // Verificação (Assert)
        // Verifica se o método `listAll` foi chamado
        verify(simulacaoRepository, times(1)).listAll();

        // Verifica se o resultado não é nulo e é a lista esperada
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(simulacoes, resultado);
    }

    @Test
    @DisplayName("Deve listar simulações por ID do cliente chamando o método list do repositório")
    void deveListarSimulacoesPorClienteId() {
        // Organização (Arrange) - O STUB FICA AQUI, dentro do teste que o usa
        when(simulacaoRepository.list("cliente.id", clienteId)).thenReturn(simulacoes);

        // Ação (Act)
        List<SimulacaoInvestimento> resultado = simulacaoInvestimentoAdapter.listarPorClienteId(clienteId);

        // Verificação (Assert)
        // Verifica se o método `list` foi chamado com os parâmetros corretos
        verify(simulacaoRepository, times(1)).list("cliente.id", clienteId);

        // Verifica se o resultado não é nulo e é a lista esperada
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(simulacoes, resultado);
    }
}