package soportec.demo.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "EQUIPO_RED", schema = "dbo")
public class EquipoRed {

    @Id
    @Column(name = "id_activo", nullable = false)
    private Integer idActivo;

    @ManyToOne
    @JoinColumn(name = "id_activo", nullable = false, insertable = false, updatable = false)
    private Activo activo;

    @ManyToOne
    @JoinColumn(name = "id_tipo_red", nullable = false)
    private CatTipoRed tipoRed;

    @Column(name = "firmware_version", length = 50)
    private String firmwareVersion;
}
