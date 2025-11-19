package br.com.seuempresa.invest.infra.adapter;

import br.com.caixaverso.invest.domain.model.TelemetriaRegistro;
import br.com.caixaverso.invest.infra.adapter.TelemetriaAdapter;
import br.com.caixaverso.invest.infra.repository.TelemetriaRegistroRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;

/**
 * Testes unitários para o {@link TelemetriaAdapter}.
 * Verifica se o adaptador interage corretamente com o repositório de telemetria.
 */
@ExtendWith(MockitoExtension.class)
class TelemetriaAdapterTest {

    @Mock
    private TelemetriaRegistroRepository telemetriaRepository;

    @InjectMocks
    private TelemetriaAdapter telemetriaAdapter;

    private TelemetriaRegistro registroDeTelemetria;

    /**
     * Método executado antes de cada teste para inicializar os objetos necessários.
     * Utiliza o builder pattern do Lombok para criar uma instância de teste.
     */
    @BeforeEach
    void setUp() {
        // Criando um objeto de exemplo usando o builder do Lombok
        registroDeTelemetria = TelemetriaRegistro.builder()
                .endpoint("/api/v1/investimentos/consultar")
                .metodoHttp("GET")
                .sucesso(true)
                .statusHttp(200)
                .duracaoMs(250)
                // O id e a dataRegistro não precisam ser setados aqui,
                // pois geralmente são gerados pelo banco de dados (persistence).
                .build();
    }

    @Test
    @DisplayName("Deve salvar um registro de telemetria chamando o método persist do repositório")
    void deveSalvarRegistroDeTelemetria() {
        // Ação (Act): Executa o método que queremos testar
        TelemetriaRegistro resultado = telemetriaAdapter.salvar(registroDeTelemetria);

        // Verificação (Assert): Verifica se o comportamento esperado ocorreu

        // 1. Verifica se o método `persist` do repositório foi chamado exatamente uma vez
        //    com o objeto `registroDeTelemetria` como argumento.
        verify(telemetriaRepository).persist(registroDeTelemetria);

        // 2. Verifica se o método `salvar` retornou o mesmo objeto que foi passado como entrada.
        assertNotNull(resultado, "O resultado não deveria ser nulo");
        assertEquals(registroDeTelemetria, resultado, "O método salvar deveria retornar o mesmo registro de entrada");
    }
}