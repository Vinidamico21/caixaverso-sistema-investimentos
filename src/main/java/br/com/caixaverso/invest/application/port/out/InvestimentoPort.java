package br.com.caixaverso.invest.application.port.out;

import br.com.caixaverso.invest.domain.model.Investimento;
import java.time.LocalDate;
import java.util.List;

public interface InvestimentoPort {

    List<Investimento> findByClienteId(Long clienteId);

    List<Investimento> findByClienteIdAndPeriodo(Long clienteId, LocalDate dataInicio);

    Investimento save(Investimento investimento);

    List<Investimento> findAll();
}