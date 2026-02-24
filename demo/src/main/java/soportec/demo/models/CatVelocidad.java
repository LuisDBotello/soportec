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
@Table(name = "CAT_VELOCIDAD", schema = "dbo")
public class CatVelocidad {

    @Id
    @Column(name = "id_velocidad")
    private Integer idVelocidad;

    @Column(name = "descripcion", nullable = false, length = 30)
    private String descripcion;

    @Column(name = "mbps", nullable = false)
    private Integer mbps;
}
