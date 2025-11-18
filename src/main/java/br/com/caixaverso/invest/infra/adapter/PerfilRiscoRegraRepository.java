package br.com.caixaverso.invest.infra.adapter;

import br.com.caixaverso.invest.domain.model.PerfilRiscoRegra;
import br.com.caixaverso.invest.domain.port.PerfilRiscoRegraPort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;

import java.util.Optional;

@ApplicationScoped
public class PerfilRiscoRegraRepository implements PerfilRiscoRegraPort {

    @Inject
    EntityManager em;

    @Override
    public Optional<PerfilRiscoRegra> buscarRegraPorScore(int score) {
        try {
            PerfilRiscoRegra regra = em.createQuery(
                            "SELECT r FROM PerfilRiscoRegra r " +
                                    "WHERE :score BETWEEN r.scoreMin AND r.scoreMax", PerfilRiscoRegra.class)
                    .setParameter("score", score)
                    .getSingleResult();
            return Optional.of(regra);
        } catch (Exception ex) {
            return Optional.empty();
        }
    }
}