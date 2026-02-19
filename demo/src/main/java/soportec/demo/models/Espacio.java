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
@Table(name = "ESPACIO")
public class Espacio {

    @Id
    @Column(name = "id_espacio")
    private Integer id_espacio;

    @ManyToOne
    @JoinColumn(name = "id_edificio", nullable = false)
    private Edificio id_edificio;

    @Column(name = "nombre", nullable = false, length = 20)
    private String nombre;
}
