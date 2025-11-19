package br.com.seuempresa.invest.domain.model;

import br.com.caixaverso.invest.domain.model.TelemetriaRegistro;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import org.hibernate.annotations.CreationTimestamp;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TelemetriaRegistroTest {

    @Test
    void testGettersAndSetters() {
        TelemetriaRegistro t = new TelemetriaRegistro();

        t.setId(1L);
        t.setEndpoint("/api/v1/teste");
        t.setMetodoHttp("GET");
        t.setSucesso(true);
        t.setStatusHttp(200);
        t.setDuracaoMs(150);
        t.setDataRegistro(LocalDateTime.of(2024, 1, 1, 12, 0));

        assertEquals(1L, t.getId());
        assertEquals("/api/v1/teste", t.getEndpoint());
        assertEquals("GET", t.getMetodoHttp());
        assertTrue(t.getSucesso());
        assertEquals(200, t.getStatusHttp());
        assertEquals(150, t.getDuracaoMs());
        assertEquals(LocalDateTime.of(2024, 1, 1, 12, 0), t.getDataRegistro());
    }

    @Test
    void testAllArgsConstructor() {
        LocalDateTime dt = LocalDateTime.now();

        TelemetriaRegistro t = new TelemetriaRegistro(
                10L,
                "/api/v1/x",
                "POST",
                false,
                500,
                300,
                dt
        );

        assertEquals(10L, t.getId());
        assertEquals("/api/v1/x", t.getEndpoint());
        assertEquals("POST", t.getMetodoHttp());
        assertFalse(t.getSucesso());
        assertEquals(500, t.getStatusHttp());
        assertEquals(300, t.getDuracaoMs());
        assertEquals(dt, t.getDataRegistro());
    }

    @Test
    void testBuilder() {
        LocalDateTime dt = LocalDateTime.of(2024, 5, 1, 8, 0);

        TelemetriaRegistro t = TelemetriaRegistro.builder()
                .id(99L)
                .endpoint("/abc")
                .metodoHttp("PUT")
                .sucesso(true)
                .statusHttp(201)
                .duracaoMs(42)
                .dataRegistro(dt)
                .build();

        assertEquals(99L, t.getId());
        assertEquals("/abc", t.getEndpoint());
        assertEquals("PUT", t.getMetodoHttp());
        assertEquals(201, t.getStatusHttp());
    }

    @Test
    void testAnnotations() throws Exception {
        Field id = TelemetriaRegistro.class.getDeclaredField("id");
        Field endpoint = TelemetriaRegistro.class.getDeclaredField("endpoint");
        Field metodo = TelemetriaRegistro.class.getDeclaredField("metodoHttp");
        Field sucesso = TelemetriaRegistro.class.getDeclaredField("sucesso");
        Field statusHttp = TelemetriaRegistro.class.getDeclaredField("statusHttp");
        Field duracaoMs = TelemetriaRegistro.class.getDeclaredField("duracaoMs");
        Field dataRegistro = TelemetriaRegistro.class.getDeclaredField("dataRegistro");

        // id
        assertNotNull(id.getAnnotation(Id.class));
        GeneratedValue gen = id.getAnnotation(GeneratedValue.class);
        assertNotNull(gen);
        assertEquals(GenerationType.IDENTITY, gen.strategy());

        // endpoint
        Column colEndpoint = endpoint.getAnnotation(Column.class);
        assertNotNull(colEndpoint);
        assertEquals(150, colEndpoint.length());
        assertFalse(colEndpoint.nullable());

        // método http
        Column colMetodo = metodo.getAnnotation(Column.class);
        assertNotNull(colMetodo);
        assertEquals("metodo_http", colMetodo.name());
        assertEquals(10, colMetodo.length());
        assertFalse(colMetodo.nullable());

        // sucesso
        Column colSucesso = sucesso.getAnnotation(Column.class);
        assertNotNull(colSucesso);
        assertFalse(colSucesso.nullable());

        // status http (não exige nullable=false)
        assertNotNull(statusHttp.getAnnotation(Column.class));

        // duração ms
        assertNotNull(duracaoMs.getAnnotation(Column.class));

        // dataRegistro
        Column colData = dataRegistro.getAnnotation(Column.class);
        assertNotNull(colData);
        assertFalse(colData.updatable());
        assertNotNull(dataRegistro.getAnnotation(CreationTimestamp.class));
    }

    @Test
    void testEqualsAndHashCode() {
        TelemetriaRegistro t1 = new TelemetriaRegistro();
        t1.setId(1L);

        TelemetriaRegistro t2 = new TelemetriaRegistro();
        t2.setId(1L);

        assertEquals(t1, t2);
        assertEquals(t1.hashCode(), t2.hashCode());

        TelemetriaRegistro t3 = new TelemetriaRegistro();
        t3.setId(2L);

        assertNotEquals(t1, t3);
    }

    @Test
    void testToString() {
        TelemetriaRegistro t = new TelemetriaRegistro();
        t.setId(5L);
        t.setEndpoint("/hello");
        t.setMetodoHttp("GET");

        String s = t.toString();

        assertTrue(s.contains("5"));
        assertTrue(s.contains("hello"));
        assertTrue(s.contains("GET"));
    }
}