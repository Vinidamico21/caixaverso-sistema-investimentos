package br.com.caixaverso.invest.domain.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Entity
@Table(name = "perfil_risco_regra")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PerfilRiscoRegra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "score_min", nullable = false)
    private Integer scoreMin;

    @Column(name = "score_max", nullable = false)
    private Integer scoreMax;

    @Column(name = "perfil", nullable = false, length = 50)
    private String perfil;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PerfilRiscoRegra that = (PerfilRiscoRegra) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "PerfilRiscoRegra{" +
                "id=" + id +
                ", scoreMin=" + scoreMin +
                ", scoreMax=" + scoreMax +
                ", perfil='" + perfil + '\'' +
                '}';
    }
}