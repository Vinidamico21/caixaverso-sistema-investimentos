package br.com.caixaverso.invest.infra.repository;

import br.com.caixaverso.invest.domain.model.Investimento;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@ApplicationScoped
public class InvestimentoRepository implements PanacheRepository<Investimento> {

    public List<Investimento> findByClienteId(Long clienteId) {
        return find("cliente.id = ?1", clienteId).list();
    }

    public List<Investimento> findByClienteIdAndPeriodo(Long clienteId, LocalDate dataInicio) {
        LocalDateTime dataInicioDateTime = dataInicio.atStartOfDay();
        return find("cliente.id = ?1 and dataAporte >= ?2",
                clienteId, dataInicioDateTime).list();
    }

    public List<Investimento> findByClienteIdAndStatus(Long clienteId, String status) {
        return find("cliente.id = ?1 and status = ?2",
                clienteId, status).list();
    }
}
