package soportec.demo.models;

import java.time.LocalDate;

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
@Table(name = "NIC", schema = "dbo")
public class Nic {

    @Id
    @Column(name = "id_nic")
    private Integer idNic;

    @ManyToOne
    @JoinColumn(name = "id_modelo_nic", nullable = false)
    private NicModelo modeloNic;

    @Column(name = "numero_serie", length = 50)
    private String numeroSerie;

    @Column(name = "mac_address", nullable = false, length = 17)
    private String macAddress;

    @Column(name = "fecha_compra")
    private LocalDate fechaCompra;

    @ManyToOne
    @JoinColumn(name = "id_activo")
    private Activo activo;
}
