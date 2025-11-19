package br.com.caixaverso.invest.application.service;

import br.com.caixaverso.invest.application.dto.InvestimentoResponseDTO;
import br.com.caixaverso.invest.application.dto.PageResponse;
import br.com.caixaverso.invest.application.port.in.ListarInvestimentosPorClienteUseCase;
import br.com.caixaverso.invest.domain.model.Investimento;
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
            LOG.warnf("Página negativa informada (%d). Normalizando para 0.", page);
            page = 0;
        }
        if (size <= 0) {
            LOG.warnf("Tamanho de página inválido (%d). Normalizando para 20.", size);
            size = 20;
        }

        LOG.infof("Buscando investimentos | clienteId=%d | page=%d | size=%d",
                clienteId, page, size);

        // Query base ordenada por dataAporte desc
        PanacheQuery<Investimento> queryBase =
                investimentoRepository.find("cliente.id = ?1 order by dataAporte desc", clienteId);

        long totalElements = queryBase.count();

        // Aplica paginação no banco
        PanacheQuery<Investimento> queryPaginada =
                queryBase.page(Page.of(page, size));

        List<Investimento> investimentos = queryPaginada.list();

        if (investimentos.isEmpty()) {
            LOG.infof("Nenhum investimento encontrado | clienteId=%d", clienteId);
        } else {
            LOG.infof("Total de investimentos retornados na página=%d | totalElements=%d | clienteId=%d",
                    investimentos.size(), totalElements, clienteId);
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
