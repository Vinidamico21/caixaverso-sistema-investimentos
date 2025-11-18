package br.com.caixaverso.invest.infra.adapter;

import br.com.caixaverso.invest.domain.model.SimulacaoInvestimento;
import br.com.caixaverso.invest.domain.port.SimulacaoInvestimentoPort;
import br.com.caixaverso.invest.infra.repository.SimulacaoInvestimentoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class SimulacaoInvestimentoAdapter implements SimulacaoInvestimentoPort {

    @Inject
    SimulacaoInvestimentoRepository simulacaoRepository;

    @Override
    public SimulacaoInvestimento salvar(SimulacaoInvestimento simulacao) {
        simulacaoRepository.persist(simulacao);
        return simulacao;
    }

    @Override
    public List<SimulacaoInvestimento> listar() {
        return simulacaoRepository.listAll();
    }

    @Override
    public List<SimulacaoInvestimento> listarPorClienteId(Long clienteId) {
        return simulacaoRepository.list("cliente.id", clienteId);
    }
}