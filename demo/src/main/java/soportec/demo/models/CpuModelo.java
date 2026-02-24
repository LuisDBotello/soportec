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
@Table(name = "CPU_MODELO", schema = "dbo")
public class CpuModelo {

    @Id
    @Column(name = "id_modelo_cpu")
    private Integer idModeloCpu;

    @ManyToOne
    @JoinColumn(name = "id_marca_cpu", nullable = false)
    private CpuMarca marcaCpu;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "nucleos", nullable = false)
    private Integer nucleos;

    @Column(name = "hilos")
    private Integer hilos;

    @Column(name = "frecuencia_base_GHz", nullable = false, precision = 4, scale = 2)
    private BigDecimal frecuenciaBaseGhz;

    @Column(name = "frecuencia_boost_GHz", precision = 4, scale = 2)
    private BigDecimal frecuenciaBoostGhz;

    @Column(name = "socket", length = 30)
    private String socket;

    @Column(name = "arquitectura", length = 50)
    private String arquitectura;
}
