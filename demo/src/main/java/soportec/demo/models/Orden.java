package soportec.demo.models;

import java.time.LocalDate;

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
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ORDEN")
public class Orden {

    @Id
    @Column(name = "folio")
    private Integer folio;

    @Column(name = "prioridad", nullable = false)
    private Integer prioridad;

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "id_periodo")
    private Periodo periodo;

    @ManyToOne
    @JoinColumn(name = "id_encargado")
    private Usuario encargado;

    @ManyToOne
    @JoinColumn(name = "id_servicio")
    private Servicio servicio;

    @ManyToOne
    @JoinColumn(name = "id_equipo")
    private Equipo equipo;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDate fecha_creacion;

    @Column(name = "fecha_cierre", nullable = true)
    private LocalDate fecha_cierre;

    @Column(name = "estatus", nullable = false, length = 30)
    private String estatus;

    @Column(name = "descripcion", length = 255)
    private String descripcion;
}
