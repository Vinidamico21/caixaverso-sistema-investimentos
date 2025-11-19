package br.com.caixaverso.invest.application.port.in;

import br.com.caixaverso.invest.application.dto.response.PageResponse;
import br.com.caixaverso.invest.application.dto.SimulacaoResumoDTO;

public interface ListarSimulacoesUseCase {

    PageResponse<SimulacaoResumoDTO> listarSimulacoes(Long clienteId, int page, int size);
}