package br.com.caixaverso.invest.infra.adapter;

import br.com.caixaverso.invest.domain.model.Investimento;
import br.com.caixaverso.invest.application.port.out.InvestimentoPort;
import br.com.caixaverso.invest.infra.repository.InvestimentoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;

@ApplicationScoped
public class InvestimentoAdapter implements InvestimentoPort {

    @Inject
    InvestimentoRepository investimentoRepository;

    @Override
    public List<Investimento> findByClienteId(Long clienteId) {
        return investimentoRepository.findByClienteId(clienteId);
    }

    @Override
    public List<Investimento> findByClienteIdAndPeriodo(Long clienteId, LocalDate dataInicio) {
        return investimentoRepository.findByClienteIdAndPeriodo(clienteId, dataInicio);
    }

    @Override
    @Transactional
    public Investimento save(Investimento investimento) {
        investimentoRepository.persist(investimento);
        return investimento;
    }

    @Override
    public List<Investimento> findAll() {
        return investimentoRepository.listAll();
    }
}