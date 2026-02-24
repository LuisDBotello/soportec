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
@Table(name = "PUERTO_RED", schema = "dbo")
public class PuertoRed {

    @Id
    @Column(name = "id_puerto")
    private Integer idPuerto;

    @ManyToOne
    @JoinColumn(name = "id_activo", nullable = false)
    private Activo activo;

    @Column(name = "numero_puerto", nullable = false)
    private Integer numeroPuerto;

    @Column(name = "nombre_logico", nullable = false, length = 50)
    private String nombreLogico;

    @ManyToOne
    @JoinColumn(name = "id_velocidad", nullable = false)
    private CatVelocidad velocidad;

    @ManyToOne
    @JoinColumn(name = "id_estado_puerto", nullable = false)
    private CatEstadoPuerto estadoPuerto;

    @Column(name = "es_poe", nullable = false)
    private Boolean esPoe;
}
