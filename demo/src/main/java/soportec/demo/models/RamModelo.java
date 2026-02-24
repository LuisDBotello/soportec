package soportec.demo.models;

import java.math.BigDecimal;

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
@Table(name = "RAM_MODELO", schema = "dbo")
public class RamModelo {

    @Id
    @Column(name = "id_modelo_ram")
    private Integer idModeloRam;

    @ManyToOne
    @JoinColumn(name = "id_marca_ram", nullable = false)
    private RamMarca marcaRam;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "tipo", nullable = false, length = 20)
    private String tipo;

    @Column(name = "capacidad_GB", nullable = false)
    private Integer capacidadGb;

    @Column(name = "velocidad_MHz", nullable = false)
    private Integer velocidadMhz;

    @Column(name = "voltaje", precision = 3, scale = 2)
    private BigDecimal voltaje;
}
