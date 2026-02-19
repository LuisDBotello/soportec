package soportec.demo.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "EDIFICIO")
public class Edificio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_EDIFICIO")
    private Integer idEdificio;

    @ManyToOne
    @JoinColumn(name = "ID_DEPTO", nullable = false)
    private Departamento departamento;

    @Column(name = "NOMBRE", nullable = false, length = 50)
    private String nombre;
}
