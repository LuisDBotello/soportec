package soportec.demo.models;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
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
@Table(name = "ACTIVO", schema = "dbo")
public class Activo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_activo")
    private Integer idActivo;

    @Column(name = "marca", nullable = false, length = 100)
    private String marca;

    @Column(name = "modelo", nullable = true, length = 100)
    private String modelo;

    @ManyToOne
    @JoinColumn(name = "id_marca_activo")
    private MarcaActivo marcaActivo;

    @ManyToOne
    @JoinColumn(name = "id_modelo_activo")
    private ModeloActivo modeloActivo;

    @Column(name = "numero_serie", nullable = false, length = 100)
    private String numeroSerie;

    @Column(name = "fecha_compra")
    private LocalDate fechaCompra;

    @ManyToOne
    @JoinColumn(name = "id_tipo_activo", nullable = false)
    private TipoActivo tipoActivo;

    @ManyToOne
    @JoinColumn(name = "id_espacio")
    private Espacio espacio;

    @ManyToOne
    @JoinColumn(name = "id_edo_activo")
    private EstadoActivo estado;
}
