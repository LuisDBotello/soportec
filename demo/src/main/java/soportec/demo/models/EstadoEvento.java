package soportec.demo.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
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
@Table(name = "ESTADO_ORD_EV")
public class EstadoEvento {
    
    @Id
    @Column(name = "id_estado", nullable = false)
    private Integer id_estado;

    @Column(name = "nombre", nullable = false, length = 40)
    private String nombre;
}
