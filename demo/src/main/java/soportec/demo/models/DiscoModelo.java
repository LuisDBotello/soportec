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
@Table(name = "DISCO_MODELO", schema = "dbo")
public class DiscoModelo {

    @Id
    @Column(name = "id_modelo_disco")
    private Integer idModeloDisco;

    @ManyToOne
    @JoinColumn(name = "id_marca_disco", nullable = false)
    private DiscoMarca marcaDisco;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "tipo_disco", nullable = false, length = 20)
    private String tipoDisco;

    @Column(name = "interfaz", nullable = false, length = 20)
    private String interfaz;

    @Column(name = "factor_forma", length = 20)
    private String factorForma;

    @Column(name = "capacidad_GB", nullable = false)
    private Integer capacidadGb;

    @Column(name = "velocidad_lectura_MBps")
    private Integer velocidadLecturaMbps;

    @Column(name = "velocidad_escritura_MBps")
    private Integer velocidadEscrituraMbps;
}
