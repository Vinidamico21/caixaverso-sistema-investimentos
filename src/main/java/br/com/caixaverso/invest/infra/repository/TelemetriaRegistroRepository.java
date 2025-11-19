package br.com.caixaverso.invest.infra.repository;

import br.com.caixaverso.invest.infra.persistence.entity.TelemetriaRegistro;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class TelemetriaRegistroRepository implements PanacheRepository<TelemetriaRegistro> {

}
