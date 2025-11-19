package br.com.caixaverso.invest.application.service;

import br.com.caixaverso.invest.application.dto.InvestimentoResponseDTO;
import br.com.caixaverso.invest.application.dto.response.PageResponse;
import br.com.caixaverso.invest.application.port.in.ListarInvestimentosPorClienteUseCase;
import br.com.caixaverso.invest.domain.constants.PerfilConstantes;
import br.com.caixaverso.invest.infra.persistence.entity.Investimento;
import br.com.caixaverso.invest.infra.repository.InvestimentoRepository;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Page;
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
    public PageResponse<InvestimentoResponseDTO> listarPorCliente(String clienteIdRaw, int page, int size) {

        Long clienteId = parseClienteIdToLong(clienteIdRaw);

        // Sanitiza parâmetros de paginação
        if (page < 0) {
            LOG.warnf(PerfilConstantes.LOG_PAGE_NEGATIVA, page);
            page = 0;
        }
        if (size <= 0) {
            LOG.warnf(PerfilConstantes.LOG_PAGE_SIZE_INVALIDO, size);
            size = 20;
        }

        LOG.infof(PerfilConstantes.LOG_BUSCA_INVESTIMENTOS, clienteId, page, size);

        // Query base
        PanacheQuery<Investimento> queryBase =
                investimentoRepository.find(PerfilConstantes.QUERY_INVESTIMENTOS_POR_CLIENTE, clienteId);

        long totalElements = queryBase.count();

        // Paginação
        PanacheQuery<Investimento> queryPaginada =
                queryBase.page(Page.of(page, size));

        List<Investimento> investimentos = queryPaginada.list();

        if (investimentos.isEmpty()) {
            LOG.infof(PerfilConstantes.LOG_INVESTIMENTOS_VAZIO, clienteId);
        } else {
            LOG.infof(
                    PerfilConstantes.LOG_INVESTIMENTOS_ENCONTRADOS,
                    investimentos.size(), totalElements, clienteId
            );
        }

        List<InvestimentoResponseDTO> content = investimentos.stream()
                .map(this::toDTO)
                .toList();

        int totalPages = totalElements == 0
                ? 0
                : (int) Math.ceil((double) totalElements / size);

        return PageResponse.<InvestimentoResponseDTO>builder()
                .content(content)
                .page(page)
                .size(size)
                .totalElements(totalElements)
                .totalPages(totalPages)
                .build();
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
