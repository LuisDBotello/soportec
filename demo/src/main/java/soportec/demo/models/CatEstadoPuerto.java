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
@Table(name = "CAT_ESTADO_PUERTO", schema = "dbo")
public class CatEstadoPuerto {

    @Id
    @Column(name = "id_estado_puerto")
    private Integer idEstadoPuerto;

    @Column(name = "nombre", nullable = false, length = 20)
    private String nombre;
}
