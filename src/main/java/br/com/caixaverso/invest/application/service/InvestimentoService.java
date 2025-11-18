package br.com.caixaverso.invest.application.service;

import br.com.caixaverso.invest.application.dto.InvestimentoResponseDTO;
import br.com.caixaverso.invest.domain.model.Investimento;
import br.com.caixaverso.invest.infra.repository.InvestimentoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;
import org.jboss.logging.MDC;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static br.com.caixaverso.invest.infra.util.ClienteIdParser.parseClienteIdToLong;

@ApplicationScoped
public class InvestimentoService {

    private static final Logger LOG = Logger.getLogger(InvestimentoService.class);

    @Inject
    InvestimentoRepository investimentoRepository;

    private String rid() {
        return (String) MDC.get("requestId");
    }

    //Lista investimentos de um cliente, validando o clienteId
    public List<InvestimentoResponseDTO> listarPorCliente(String clienteIdRaw) {

        Long clienteId = parseClienteIdToLong(clienteIdRaw);

        LOG.infof("[reqId=%s] Buscando investimentos | clienteId=%d", rid(), clienteId);

        List<Investimento> investimentos = investimentoRepository.list(
                "cliente.id = ?1 order by dataAporte desc", clienteId
        );

        return investimentos.stream()
                .map(this::toDTO)
                .toList();
    }

    private InvestimentoResponseDTO toDTO(Investimento inv) {
        String tipo = inv.getProduto() != null ? inv.getProduto().getTipo() : null;

        BigDecimal rentabilidade = inv.getProduto() != null
                ? inv.getProduto().getTaxaAnual()
                : null;

        LocalDate data = inv.getDataAporte() != null
                ? inv.getDataAporte().toLocalDate()
                : null;

        return InvestimentoResponseDTO.builder()
                .id(inv.getId())
                .tipo(tipo)
                .valor(inv.getValorAplicado())
                .rentabilidade(rentabilidade)
                .data(data)
                .build();
    }
}
