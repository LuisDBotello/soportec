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
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "EVALUACION")
public class Evaluacion {

    @Id
    @Column(name = "id_evaluacion")
    private Integer id_evaluacion;

    @ManyToOne
    @JoinColumn(name = "folio", referencedColumnName = "folio", nullable = false)
    private Orden folio;

    @Column(name = "calificacion")
    private Integer calificacion;

    @Column(name = "comentario", length = 255)
    private String comentario;

    @Column(name = "fecha")
    private LocalDate fecha;
}
