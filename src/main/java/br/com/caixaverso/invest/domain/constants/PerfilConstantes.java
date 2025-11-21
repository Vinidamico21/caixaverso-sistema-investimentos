package br.com.caixaverso.invest.domain.constants;

import java.math.BigDecimal;

public final class PerfilConstantes {

    private PerfilConstantes() {}

    /* ---------------------------- PERFIS ---------------------------- */

    public static final String PERFIL_CONSERVADOR = "CONSERVADOR";
    public static final String PERFIL_MODERADO = "MODERADO";
    public static final String PERFIL_AGRESSIVO = "AGRESSIVO";
    public static final String PERFIL_DESCONHECIDO = "DESCONHECIDO";

    public static final String PERFIL_CONSERVADOR_PRODUTO = "BAIXO";
    public static final String PERFIL_MODERADO_PRODUTO = "MEDIO";
    public static final String PERFIL_AGRESSIVO_PRODUTO = "ALTO";

    /* ---------------------------- DESCRIÇÕES ---------------------------- */

    public static final String DESC_CONSERVADOR = "Busca segurança e liquidez.";
    public static final String DESC_MODERADO = "Equilíbrio entre liquidez e rentabilidade.";
    public static final String DESC_AGRESSIVO = "Busca alta rentabilidade, aceitando maior risco.";
    public static final String DESC_DESCONHECIDO = "Perfil não classificado.";

    /* ---------------------------- PREFERÊNCIAS ---------------------------- */

    public static final String PREF_LIQUIDEZ = "LIQUIDEZ";
    public static final String PREF_RENTABILIDADE = "RENTABILIDADE";
    public static final String PREF_EMPATE = "EMPATE";

    /* ---------------------------- LIQUIDEZ ---------------------------- */

    public static final String LIQ_DIARIA = "DIARIA";
    public static final String LIQ_D0 = "D+0";
    public static final String LIQ_MENSAL = "MENSAL";
    public static final String LIQ_D30 = "D+30";

    /* ---------------------------- RENTABILIDADE ---------------------------- */

    public static final BigDecimal RENTABILIDADE_BAIXA = new BigDecimal("0.08");
    public static final BigDecimal RENTABILIDADE_MEDIA = new BigDecimal("0.12");

    /* ---------------------------- PRAZO ---------------------------- */

    public static final int PRAZO_CURTO = 3;
    public static final int PRAZO_MEDIO = 12;

    /* ---------------------------- SCORES ---------------------------- */

    public static final BigDecimal SCORE_BAIXO = BigDecimal.ONE;
    public static final BigDecimal SCORE_MEDIO = new BigDecimal("2");
    public static final BigDecimal SCORE_ALTO = new BigDecimal("3");

    /* ---------------------------- PESOS DE PERFIL ---------------------------- */

    public static final BigDecimal PESO_LIQ_CONSERVADOR = new BigDecimal("0.6");
    public static final BigDecimal PESO_RENT_CONSERVADOR = new BigDecimal("0.2");
    public static final BigDecimal PESO_RISCO_CONSERVADOR = new BigDecimal("0.2");

    public static final BigDecimal PESO_LIQ_MODERADO = new BigDecimal("0.4");
    public static final BigDecimal PESO_RENT_MODERADO = new BigDecimal("0.4");
    public static final BigDecimal PESO_RISCO_MODERADO = new BigDecimal("0.2");

    public static final BigDecimal PESO_LIQ_AGRESSIVO = new BigDecimal("0.2");
    public static final BigDecimal PESO_RENT_AGRESSIVO = new BigDecimal("0.6");
    public static final BigDecimal PESO_RISCO_AGRESSIVO = new BigDecimal("0.2");

    /* ---------------------------- AUTH ---------------------------- */

    public static final String AUTH_ERRO_CREDENCIAIS_OBRIGATORIAS = "username e password são obrigatórios";
    public static final String AUTH_ERRO_CREDENCIAIS_INVALIDAS = "Credenciais inválidas";
    public static final String AUTH_USER_ADMIN = "admin";
    public static final String AUTH_USER_PADRAO = "user";
    public static final String AUTH_ROLE_ADMIN = "ADMIN";
    public static final String AUTH_ROLE_USER = "USER";
    public static final String AUTH_JWT_ISSUER = "caixaverso-investimentos";
    public static final String AUTH_JWT_TOKEN_TYPE = "Bearer";
    public static final String AUTH_JWT_TOKEN_BLOCKED = "Usuário temporariamente bloqueado por tentativas inválidas";

    /* ---------------------------- INVESTIMENTOS ---------------------------- */

    public static final String LOG_PAGE_NEGATIVA = "Página negativa informada (%d). Normalizando para 0.";
    public static final String LOG_PAGE_SIZE_INVALIDO = "Tamanho de página inválido (%d). Normalizando para 20.";
    public static final String LOG_BUSCA_INVESTIMENTOS = "Buscando investimentos | clienteId=%d | page=%d | size=%d";
    public static final String LOG_INVESTIMENTOS_VAZIO = "Nenhum investimento encontrado | clienteId=%d";
    public static final String LOG_INVESTIMENTOS_ENCONTRADOS = "Total de investimentos retornados na página=%d | totalElements=%d | clienteId=%d";
    public static final String QUERY_INVESTIMENTOS_POR_CLIENTE = "cliente.id = ?1 order by dataAporte desc";

    /* ---------------------------- PERFIL DE RISCO - LOG ---------------------------- */

    public static final String LOG_CALC_INICIO = "Iniciando calculo de perfil | clienteId=%d";
    public static final String LOG_CALC_SCORE_FINAL = "Score final=%d (preferencia=%d, volume=%d, frequencia=%d)";
    public static final String LOG_CALC_CLASSIFICADO = "Perfil classificado=%s";

    /* ---------------------------- RECOMENDAÇÃO - LOG ---------------------------- */

    public static final String LOG_RECOM_INICIO = "Iniciando recomendacao | clienteId=%d";
    public static final String LOG_RECOM_PRODUTOS_CARREGADOS = "Produtos carregados=%d | perfil=%s";
    public static final String LOG_RECOM_TOTAL = "Total recomendados=%d";
    public static final String LOG_RECOM_PERFIL_NAO_ENCONTRADO = "Perfil nao encontrado para recomendacao | clienteId=%d";
    public static final String LOG_RECOM_DIRETA = "Recomendacao direta por perfil=%s";

    /* ---------------------------- PIPELINE - LOG ---------------------------- */

    public static final String LOG_PIPELINE_INICIO = "Iniciando pipeline de recomendação | perfil=%s | totalProdutos=%d";
    public static final String LOG_PIPELINE_PRODUTO = "Produto | id=%d | nome=%s | taxa=%s";
    public static final String LOG_PIPELINE_RISCO = "Risco | %s -> %s";
    public static final String LOG_PIPELINE_SELECIONADO = "Selecionado | %s | score=%s";

    /* ---------------------------- LIMITES ---------------------------- */

    public static final int LIMITE_PADRAO_RECOMENDACAO = 5;

    /* ---------------------------- EXCEÇÕES GERAIS ---------------------------- */

    public static final String ERRO_CLIENTE_ID_OBRIGATORIO = "Parâmetro 'clienteId' é obrigatório.";
    public static final String ERRO_CLIENTE_ID_INVALIDO = "Parâmetro 'clienteId' deve ser maior que zero.";
    public static final String ERRO_CLIENTE_NAO_ENCONTRADO = "Cliente não encontrado.";
    public static final String ERRO_PRODUTO_TIPO_NAO_ENCONTRADO = "Nenhum produto encontrado para o tipo solicitado.";

    /* ---------------------------- SIMULAÇÃO - LOG ---------------------------- */

    public static final String LOG_SIM_INICIO = "Iniciando simulacao | clienteId=%d | tipoProduto=%s | valor=%s | prazoMeses=%d";
    public static final String LOG_SIM_CLIENTE_NAO_ENCONTRADO = "Cliente nao encontrado para simulacao | clienteId=%d";
    public static final String LOG_SIM_CLIENTE_ENCONTRADO = "Cliente encontrado para simulacao | clienteId=%d";
    public static final String LOG_SIM_PRODUTOS_NAO_ENCONTRADOS = "Nenhum produto encontrado para o tipo solicitado | tipoProduto=%s";
    public static final String LOG_SIM_PRODUTOS_ENCONTRADOS = "Produtos encontrados para simulacao=%d | tipoProduto=%s";
    public static final String LOG_SIM_MELHOR_PRODUTO = "Melhor produto selecionado | produtoId=%d | nome=%s | taxaAnual=%s";
    public static final String LOG_SIM_RESULTADO = "Resultado da simulacao | valorInicial=%s | taxaAnual=%s | prazoMeses=%d | valorFinal=%s";
    public static final String LOG_SIM_PERSISTENCIA = "Persistindo simulacao de investimento | clienteId=%d | produtoId=%d | valorAplicado=%s | valorFinal=%s";
    public static final String LOG_SIM_SALVA = "Simulacao de investimento salva com sucesso.";
    public static final String LOG_SIM_FINAL = "Simulacao concluida | clienteId=%d | produto=%s | valorFinal=%s | prazoMeses=%d";
    public static final String LOG_SIM_MONTANDO_RESPOSTA = "Montando resposta de simulacao | produtoId=%d | valorFinal=%s | prazoMeses=%d";
    public static final String LOG_SIM_RESPOSTA_OK = "Resposta de simulacao montada com sucesso.";

    /* ---------------------------- LISTAGEM SIMULAÇÕES ---------------------------- */

    public static final String LOG_LISTAR_SIM_INICIO = "Listando simulacoes | filtroClienteId=%s | page=%d | size=%d";
    public static final String LOG_LISTAR_SIM_TOTAL = "Total de simulacoes encontradas=%d";
    public static final String LOG_LISTAR_SIM_ITEM = "Simulacao encontrada | id=%d | clienteId=%d | produto=%s | valorAplicado=%s | valorFinal=%s";
    public static final String LOG_LISTAR_SIM_PAGINA = "Pagina gerada | page=%d | size=%d | totalElements=%d | totalPages=%d";

    /* ---------------------------- AGRUPAMENTO SIMULAÇÕES ---------------------------- */

    public static final String LOG_AGRUP_INICIO = "Iniciando agrupamento de simulacoes por produto e dia.";
    public static final String LOG_AGRUP_TOTAL = "Total de simulacoes carregadas para agrupamento=%d";
    public static final String LOG_AGRUP_PROCESSANDO_PRODUTO = "Processando agrupamento para produto=%s";
    public static final String LOG_AGRUP_REGISTRO = "Agrupamento gerado | produto=%s | data=%s | qtdSimulacoes=%d | mediaValorFinal=%s";
    public static final String LOG_AGRUP_FINAL = "Total de registros de agrupamento por produto/dia gerados=%d";

    /* ---------------------------- CÁLCULO JUROS ---------------------------- */

    public static final String LOG_JUROS_INICIO = "Calculando juros compostos | valor=%s | taxaAnual=%s | meses=%d";
    public static final String LOG_JUROS_RESULTADO = "Juros compostos calculados | resultado=%s";

    /* ---------------------------- TELEMETRIA EXCEÇÕES ---------------------------- */

    public static final String ERR_TELEMETRIA_ENDPOINT_OBRIGATORIO = "Endpoint de telemetria é obrigatório.";
    public static final String ERR_TELEMETRIA_METODO_OBRIGATORIO = "Método HTTP de telemetria é obrigatório.";
    public static final String ERR_TELEMETRIA_STATUS_INVALIDO = "Status HTTP inválido na telemetria: ";
    public static final String ERR_TELEMETRIA_DURACAO_NEGATIVA = "Duração da requisição (ms) não pode ser negativa.";

    /* ---------------------------- TELEMETRIA LOGS ---------------------------- */

    public static final String LOG_TELEMETRIA_REGISTRANDO = "Registrando telemetria | endpoint=%s | metodo=%s | status=%d | duracaoMs=%d";
    public static final String LOG_TELEMETRIA_RELATORIO_INICIO = "Gerando relatório de telemetria";
    public static final String LOG_TELEMETRIA_RELATORIO_FIM = "Relatório de telemetria gerado | servicos=%d | periodo=%s até %s";
}
