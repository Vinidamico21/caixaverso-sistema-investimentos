package br.com.caixaverso.invest.application.service;

import br.com.caixaverso.invest.application.dto.InvestimentoResponseDTO;
import br.com.caixaverso.invest.domain.model.Investimento;
import br.com.caixaverso.invest.application.port.in.ListarInvestimentosPorClienteUseCase;
import br.com.caixaverso.invest.infra.repository.InvestimentoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static br.com.caixaverso.invest.infra.util.ClienteIdParser.parseClienteIdToLong;

@ApplicationScoped
public class InvestimentoService implements ListarInvestimentosPorClienteUseCase {

    private static final Logger LOG = Logger.getLogger(InvestimentoService.class);

    @Inject
    InvestimentoRepository investimentoRepository;

    @Override
    public List<InvestimentoResponseDTO> listarPorCliente(String clienteIdRaw) {

        Long clienteId = parseClienteIdToLong(clienteIdRaw);

        LOG.infof("Buscando investimentos | clienteId=%d", clienteId);

        List<Investimento> investimentos = investimentoRepository.list(
                "cliente.id = ?1 order by dataAporte desc", clienteId
        );

        if (investimentos.isEmpty()) {
            LOG.infof("Nenhum investimento encontrado | clienteId=%d", clienteId);
        } else {
            LOG.infof("Total de investimentos encontrados=%d | clienteId=%d",
                    investimentos.size(), clienteId);
        }

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
