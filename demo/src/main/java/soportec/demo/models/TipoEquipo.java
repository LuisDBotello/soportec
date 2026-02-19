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
@Table(name = "TIPO_EQUIPO")
public class TipoEquipo {

    @Id
    @Column(name = "id_tipo")
    private Integer id_tipo_equipo;

    @ManyToOne
    @JoinColumn(name = "id_categoria", nullable = false)
    private EquipoCategoria id_categoria;

    @Column(name = "nombre", nullable = false, length = 50)
    private String nombre;

    @Column(name = "descripcion", nullable = true, length = 150)
    private String descripcion;
}
