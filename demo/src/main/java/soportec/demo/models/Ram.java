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
@Table(name = "RAM", schema = "dbo")
public class Ram {

    @Id
    @Column(name = "id_ram")
    private Integer idRam;

    @ManyToOne
    @JoinColumn(name = "id_modelo_ram", nullable = false)
    private RamModelo modeloRam;

    @Column(name = "numero_serie", length = 50)
    private String numeroSerie;

    @Column(name = "fecha_compra")
    private LocalDate fechaCompra;

    @ManyToOne
    @JoinColumn(name = "id_activo")
    private Activo activo;
}
