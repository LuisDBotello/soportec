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
@Table(name = "TIPO_ACTIVO", schema = "dbo")
public class TipoActivo {

    @Id
    @Column(name = "id_tipo_activo")
    private Integer idTipoActivo;

    @ManyToOne
    @JoinColumn(name = "id_categoria_activo", nullable = false)
    private CatActivo categoriaActivo;

    @Column(name = "nombre", nullable = false, length = 50)
    private String nombre;
}
