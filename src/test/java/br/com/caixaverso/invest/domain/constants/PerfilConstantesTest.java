package br.com.caixaverso.invest.domain.constants;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class PerfilConstantesTest {

    @Test
    void testPrivateConstructor() throws Exception {
        Constructor<PerfilConstantes> constructor =
                PerfilConstantes.class.getDeclaredConstructor();

        assertTrue(Modifier.isPrivate(constructor.getModifiers()));
        constructor.setAccessible(true);
        constructor.newInstance();
    }

    /* ========================= PERFIS ========================= */
    @Test
    void testPerfis() {
        assertEquals("CONSERVADOR", PerfilConstantes.PERFIL_CONSERVADOR);
        assertEquals("MODERADO", PerfilConstantes.PERFIL_MODERADO);
        assertEquals("AGRESSIVO", PerfilConstantes.PERFIL_AGRESSIVO);
        assertEquals("DESCONHECIDO", PerfilConstantes.PERFIL_DESCONHECIDO);
    }

    @Test
    void testPerfisProduto() {
        assertEquals("BAIXO", PerfilConstantes.PERFIL_CONSERVADOR_PRODUTO);
        assertEquals("MEDIO", PerfilConstantes.PERFIL_MODERADO_PRODUTO);
        assertEquals("ALTO", PerfilConstantes.PERFIL_AGRESSIVO_PRODUTO);
    }

    /* ========================= DESCRIÇÕES ========================= */
    @Test
    void testDescricoes() {
        assertEquals("Busca segurança e liquidez.", PerfilConstantes.DESC_CONSERVADOR);
        assertEquals("Equilíbrio entre liquidez e rentabilidade.", PerfilConstantes.DESC_MODERADO);
        assertEquals("Busca alta rentabilidade, aceitando maior risco.", PerfilConstantes.DESC_AGRESSIVO);
        assertEquals("Perfil não classificado.", PerfilConstantes.DESC_DESCONHECIDO);
    }

    /* ========================= PREFERÊNCIAS ========================= */
    @Test
    void testPreferencias() {
        assertEquals("LIQUIDEZ", PerfilConstantes.PREF_LIQUIDEZ);
        assertEquals("RENTABILIDADE", PerfilConstantes.PREF_RENTABILIDADE);
        assertEquals("EMPATE", PerfilConstantes.PREF_EMPATE);
    }

    /* ========================= LIQUIDEZ ========================= */
    @Test
    void testLiquidezStrings() {
        assertEquals("DIARIA", PerfilConstantes.LIQ_DIARIA);
        assertEquals("D+0", PerfilConstantes.LIQ_D0);
        assertEquals("MENSAL", PerfilConstantes.LIQ_MENSAL);
        assertEquals("D+30", PerfilConstantes.LIQ_D30);
    }

    /* ========================= RENTABILIDADE ========================= */
    @Test
    void testRentabilidadeValores() {
        assertEquals(new BigDecimal("0.08"), PerfilConstantes.RENTABILIDADE_BAIXA);
        assertEquals(new BigDecimal("0.12"), PerfilConstantes.RENTABILIDADE_MEDIA);
    }

    /* ========================= PRAZO ========================= */
    @Test
    void testPrazos() {
        assertEquals(3, PerfilConstantes.PRAZO_CURTO);
        assertEquals(12, PerfilConstantes.PRAZO_MEDIO);
    }

    /* ========================= SCORES ========================= */
    @Test
    void testScores() {
        assertEquals(BigDecimal.ONE, PerfilConstantes.SCORE_BAIXO);
        assertEquals(new BigDecimal("2"), PerfilConstantes.SCORE_MEDIO);
        assertEquals(new BigDecimal("3"), PerfilConstantes.SCORE_ALTO);
    }

    /* ========================= PESOS ========================= */
    @Test
    void testPesosConservador() {
        assertEquals(new BigDecimal("0.6"), PerfilConstantes.PESO_LIQ_CONSERVADOR);
        assertEquals(new BigDecimal("0.2"), PerfilConstantes.PESO_RENT_CONSERVADOR);
        assertEquals(new BigDecimal("0.2"), PerfilConstantes.PESO_RISCO_CONSERVADOR);
    }

    @Test
    void testPesosModerado() {
        assertEquals(new BigDecimal("0.4"), PerfilConstantes.PESO_LIQ_MODERADO);
        assertEquals(new BigDecimal("0.4"), PerfilConstantes.PESO_RENT_MODERADO);
        assertEquals(new BigDecimal("0.2"), PerfilConstantes.PESO_RISCO_MODERADO);
    }

    @Test
    void testPesosAgressivo() {
        assertEquals(new BigDecimal("0.2"), PerfilConstantes.PESO_LIQ_AGRESSIVO);
        assertEquals(new BigDecimal("0.6"), PerfilConstantes.PESO_RENT_AGRESSIVO);
        assertEquals(new BigDecimal("0.2"), PerfilConstantes.PESO_RISCO_AGRESSIVO);
    }

    /* ========================= LIMITES ========================= */
    @Test
    void testLimites() {
        assertEquals(5, PerfilConstantes.LIMITE_PADRAO_RECOMENDACAO);
    }

    /* ========================= EXCEÇÕES ========================= */
    @Test
    void testExcecoes() {
        assertEquals("Parâmetro 'clienteId' é obrigatório.", PerfilConstantes.ERRO_CLIENTE_ID_OBRIGATORIO);
        assertEquals("Parâmetro 'clienteId' deve ser maior que zero.", PerfilConstantes.ERRO_CLIENTE_ID_INVALIDO);
        assertEquals("Cliente não encontrado.", PerfilConstantes.ERRO_CLIENTE_NAO_ENCONTRADO);
        assertEquals("Nenhum produto encontrado para o tipo solicitado.", PerfilConstantes.ERRO_PRODUTO_TIPO_NAO_ENCONTRADO);
    }

    @Test
    void testExcecoesTelemetria() {
        assertEquals("Endpoint de telemetria é obrigatório.", PerfilConstantes.ERR_TELEMETRIA_ENDPOINT_OBRIGATORIO);
        assertEquals("Método HTTP de telemetria é obrigatório.", PerfilConstantes.ERR_TELEMETRIA_METODO_OBRIGATORIO);
        assertTrue(PerfilConstantes.ERR_TELEMETRIA_STATUS_INVALIDO.contains("Status HTTP inválido"));
        assertEquals("Duração da requisição (ms) não pode ser negativa.", PerfilConstantes.ERR_TELEMETRIA_DURACAO_NEGATIVA);
    }

    /* ========================= AUTH ========================= */
    @Test
    void testAuthConstants() {
        assertEquals("username e password são obrigatórios", PerfilConstantes.AUTH_ERRO_CREDENCIAIS_OBRIGATORIAS);
        assertEquals("Credenciais inválidas", PerfilConstantes.AUTH_ERRO_CREDENCIAIS_INVALIDAS);
        assertEquals("admin", PerfilConstantes.AUTH_USER_ADMIN);
        assertEquals("user", PerfilConstantes.AUTH_USER_PADRAO);
        assertEquals("ADMIN", PerfilConstantes.AUTH_ROLE_ADMIN);
        assertEquals("USER", PerfilConstantes.AUTH_ROLE_USER);
        assertEquals("caixaverso-investimentos", PerfilConstantes.AUTH_JWT_ISSUER);
        assertEquals("Bearer", PerfilConstantes.AUTH_JWT_TOKEN_TYPE);
    }

    /* ========================= QUERY ========================= */
    @Test
    void testQueryInvestimentos() {
        assertEquals("cliente.id = ?1 order by dataAporte desc",
                PerfilConstantes.QUERY_INVESTIMENTOS_POR_CLIENTE);
    }

    /* ========================= LOGS ALEATÓRIOS ========================= */
    @Test
    void testLogsExistem() {
        assertNotNull(PerfilConstantes.LOG_CALC_INICIO);
        assertNotNull(PerfilConstantes.LOG_RECOM_INICIO);
        assertNotNull(PerfilConstantes.LOG_PIPELINE_INICIO);
        assertNotNull(PerfilConstantes.LOG_SIM_INICIO);
        assertNotNull(PerfilConstantes.LOG_LISTAR_SIM_INICIO);
        assertNotNull(PerfilConstantes.LOG_AGRUP_INICIO);
        assertNotNull(PerfilConstantes.LOG_JUROS_INICIO);
        assertNotNull(PerfilConstantes.LOG_TELEMETRIA_REGISTRANDO);
        assertNotNull(PerfilConstantes.LOG_TELEMETRIA_RELATORIO_INICIO);
    }
}
