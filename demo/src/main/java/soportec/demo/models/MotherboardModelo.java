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
@Table(name = "MOTHERBOARD_MODELO", schema = "dbo")
public class MotherboardModelo {

    @Id
    @Column(name = "id_modelo_motherboard")
    private Integer idModeloMotherboard;

    @ManyToOne
    @JoinColumn(name = "id_marca_motherboard", nullable = false)
    private MotherboardMarca marcaMotherboard;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "socket", nullable = false, length = 30)
    private String socket;

    @Column(name = "chipset", nullable = false, length = 50)
    private String chipset;

    @Column(name = "factor_forma", nullable = false, length = 20)
    private String factorForma;

    @Column(name = "tipo_memoria", nullable = false, length = 20)
    private String tipoMemoria;

    @Column(name = "max_memoria_GB")
    private Integer maxMemoriaGb;

    @Column(name = "ranuras_ram")
    private Integer ranurasRam;

    @Column(name = "soporte_m2")
    private Boolean soporteM2;

    @Column(name = "soporte_sata")
    private Integer soporteSata;
}
