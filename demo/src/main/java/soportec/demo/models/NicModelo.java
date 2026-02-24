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
@Table(name = "NIC_MODELO", schema = "dbo")
public class NicModelo {

    @Id
    @Column(name = "id_modelo_nic")
    private Integer idModeloNic;

    @ManyToOne
    @JoinColumn(name = "id_marca_nic", nullable = false)
    private NicMarca marcaNic;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "tipo_interfaz", nullable = false, length = 30)
    private String tipoInterfaz;

    @Column(name = "tipo_conexion", nullable = false, length = 30)
    private String tipoConexion;

    @Column(name = "velocidad_maxima", nullable = false, length = 20)
    private String velocidadMaxima;

    @Column(name = "soporta_wifi", nullable = false)
    private Boolean soportaWifi;

    @Column(name = "frecuencia_wifi", length = 30)
    private String frecuenciaWifi;

    @Column(name = "soporta_vlan", nullable = false)
    private Boolean soportaVlan;
}
