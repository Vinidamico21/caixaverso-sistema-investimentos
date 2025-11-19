package br.com.caixaverso.invest.application.dto;

import br.com.caixaverso.invest.application.dto.response.PageResponse;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PageResponseTest {

    @Test
    void deveConstruirComBuilderEManterValores() {
        List<String> content = List.of("a", "b", "c");

        PageResponse<String> page = PageResponse.<String>builder()
                .content(content)
                .page(2)
                .size(10)
                .totalElements(30L)
                .totalPages(3)
                .build();

        assertEquals(content, page.getContent());
        assertEquals(2, page.getPage());
        assertEquals(10, page.getSize());
        assertEquals(30L, page.getTotalElements());
        assertEquals(3, page.getTotalPages());
    }

    @Test
    void devePermitirAlteracaoViaSetters() {
        PageResponse<String> page = new PageResponse<>(null, 0, 0, 0L, 0);

        List<String> content = List.of("x", "y");
        page.setContent(content);
        page.setPage(1);
        page.setSize(5);
        page.setTotalElements(10L);
        page.setTotalPages(2);

        assertEquals(content, page.getContent());
        assertEquals(1, page.getPage());
        assertEquals(5, page.getSize());
        assertEquals(10L, page.getTotalElements());
        assertEquals(2, page.getTotalPages());
    }

    @Test
    void equalsEHashCodeDevemConsiderarMesmosCampos() {
        List<String> content = List.of("a", "b");

        PageResponse<String> p1 = new PageResponse<>(content, 1, 10, 20L, 2);
        PageResponse<String> p2 = new PageResponse<>(content, 1, 10, 20L, 2);
        PageResponse<String> p3 = new PageResponse<>(content, 2, 10, 20L, 2); // página diferente

        // reflexivo
        assertEquals(p1, p1);

        // simétrico e consistente
        assertEquals(p1, p2);
        assertEquals(p2, p1);
        assertEquals(p1.hashCode(), p2.hashCode());

        // diferente
        assertNotEquals(p1, p3);
        assertNotEquals(p1, null);
        assertNotEquals(p1, "alguma string");
    }

    @Test
    void toStringDeveRetornarRepresentacaoTexto() {
        PageResponse<String> page = new PageResponse<>(List.of("a"), 1, 10, 1L, 1);

        String str = page.toString();

        assertNotNull(str);
        assertTrue(str.contains("page=1"));
        assertTrue(str.contains("size=10"));
    }
}
