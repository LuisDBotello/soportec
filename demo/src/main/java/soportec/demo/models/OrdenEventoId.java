package soportec.demo.models;

import java.io.Serializable;
import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrdenEventoId implements Serializable {

    private Integer id_evento;
    private Integer folio;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OrdenEventoId that = (OrdenEventoId) o;
        return Objects.equals(id_evento, that.id_evento) && Objects.equals(folio, that.folio);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id_evento, folio);
    }
}
