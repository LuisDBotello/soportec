package soportec.demo.models;

import java.time.LocalDateTime;

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
@Table(name = "CONEXION_RED", schema = "dbo")
public class ConexionRed {

    @Id
    @Column(name = "id_conexion")
    private Integer idConexion;

    @ManyToOne
    @JoinColumn(name = "id_puerto_origen", nullable = false)
    private PuertoRed puertoOrigen;

    @ManyToOne
    @JoinColumn(name = "id_puerto_destino", nullable = false)
    private PuertoRed puertoDestino;

    @ManyToOne
    @JoinColumn(name = "id_tipo_enlace", nullable = false)
    private CatTipoEnlace tipoEnlace;

    @Column(name = "fecha_conexion", nullable = false)
    private LocalDateTime fechaConexion;
}
