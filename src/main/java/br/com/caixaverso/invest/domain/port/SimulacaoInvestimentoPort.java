package br.com.caixaverso.invest.domain.port;

import br.com.caixaverso.invest.domain.model.SimulacaoInvestimento;
import java.util.List;

public interface SimulacaoInvestimentoPort {
    SimulacaoInvestimento salvar(SimulacaoInvestimento simulacao);
    List<SimulacaoInvestimento> listar();
    List<SimulacaoInvestimento> listarPorClienteId(Long clienteId);
}