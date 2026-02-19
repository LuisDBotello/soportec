package soportec.demo.models;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
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
@IdClass(OrdenEventoId.class)
@Table(name = "ORDEN_EVENTO")
public class OrdenEvento {

    @Id
    @ManyToOne
    @JoinColumn(name = "id_evento", referencedColumnName = "id", nullable = false)
    private Evento id_evento;

    @Id
    @ManyToOne
    @JoinColumn(name = "folio", referencedColumnName = "folio", nullable = false)
    private Orden folio;

    @Column(name = "fecha")
    private LocalDate fecha;

    @Column(name = "comentario")
    private String comentario;

    @ManyToOne
    @JoinColumn(name = "estado", referencedColumnName = "id_estado")
    private EstadoEvento estadoEvento;
}
