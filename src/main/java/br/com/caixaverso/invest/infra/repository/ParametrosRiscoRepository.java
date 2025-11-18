package br.com.caixaverso.invest.infra.repository;

import br.com.caixaverso.invest.domain.model.ParametrosRisco;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ParametrosRiscoRepository implements PanacheRepository<ParametrosRisco> {

}
