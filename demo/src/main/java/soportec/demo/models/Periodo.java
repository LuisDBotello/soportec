package soportec.demo.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "PERIODO")
public class Periodo {

    @Id
    @Column(name = "id_periodo")
    private Integer id_periodo;

    @Column(name = "nombre_periodo", nullable = false, length = 20)
    private String nombre;
}
