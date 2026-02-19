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
@Table(name = "DEPARTAMENTO")
public class Departamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_depto")
    private Integer idDepartamento;

    @Column(name = "nombreDepto", nullable = false, length = 100)
    private String nombreDepto;

    @ManyToOne
    @JoinColumn(name = "id_organizacion", nullable = false)
    private Organizacion organizacion;
}
