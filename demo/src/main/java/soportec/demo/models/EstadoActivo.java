package soportec.demo.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "ESTADO_ACTIVO")
public class EstadoActivo {

    @Id
    @Column(name = "id_edo_activo", nullable = false)
    private Integer idEdoActivo;
    
    @Column(name = "nombre", nullable = false, length = 30)
    private String nombre;
}
