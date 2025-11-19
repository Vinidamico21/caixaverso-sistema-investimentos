package br.com.caixaverso.invest.infra.adapter;

import br.com.caixaverso.invest.domain.model.PerfilRiscoRegra;
import io.quarkus.cache.CacheKey;
import io.quarkus.cache.CacheResult;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.NoResultException;

import java.util.Optional;

@ApplicationScoped
public class PerfilRiscoRegraRepository implements PanacheRepository<PerfilRiscoRegra> {

    @CacheResult(cacheName = "perfil-risco-por-score")
    public Optional<PerfilRiscoRegra> buscarPorScore(@CacheKey int score) {
        try {
            PerfilRiscoRegra regra = getEntityManager().createQuery(
                            "SELECT r FROM PerfilRiscoRegra r " +
                                    "WHERE :score BETWEEN r.scoreMin AND r.scoreMax", PerfilRiscoRegra.class)
                    .setParameter("score", score)
                    .getSingleResult();
            return Optional.of(regra);
        } catch (NoResultException ex) {
            return Optional.empty();
        }
    }
}