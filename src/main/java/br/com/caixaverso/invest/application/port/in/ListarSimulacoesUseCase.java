package br.com.caixaverso.invest.application.port.in;

import br.com.caixaverso.invest.application.dto.SimulacaoResumoDTO;

import java.util.List;

public interface ListarSimulacoesUseCase {

    List<SimulacaoResumoDTO> listarSimulacoes(Long clienteId);

}

