package soportec.demo.models.ids;

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
public class ActivoSoftwareId implements Serializable {

    private Integer activo;
    private Integer software;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ActivoSoftwareId that = (ActivoSoftwareId) o;
        return Objects.equals(activo, that.activo) && Objects.equals(software, that.software);
    }

    @Override
    public int hashCode() {
        return Objects.hash(activo, software);
    }
}
