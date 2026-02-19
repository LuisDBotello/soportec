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
@Table(name = "EQUIPO")
public class Equipo {

    @Id
    @Column(name = "id_equipo")
    private Integer id_equipo;

    @ManyToOne
    @JoinColumn(name = "id_tipo", nullable = false)
    private TipoEquipo id_tipo;

    @ManyToOne
    @JoinColumn(name = "id_marca")
    private Marca id_marca;

    @Column(name = "modelo", nullable = true, length = 50)
    private String modelo;

    @Column(name = "estado", nullable = false, length = 30)
    private String estado;

    @ManyToOne
    @JoinColumn(name = "id_depto")
    private Departamento id_depto;
}
