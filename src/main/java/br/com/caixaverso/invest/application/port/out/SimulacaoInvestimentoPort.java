package br.com.caixaverso.invest.application.port.out;

import br.com.caixaverso.invest.infra.persistence.entity.SimulacaoInvestimento;
import java.util.List;

public interface SimulacaoInvestimentoPort {
    SimulacaoInvestimento salvar(SimulacaoInvestimento simulacao);
    List<SimulacaoInvestimento> listar();
    List<SimulacaoInvestimento> listarPorClienteId(Long clienteId);
}