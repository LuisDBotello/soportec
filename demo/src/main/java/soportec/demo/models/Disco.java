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
@Table(name = "DISCO", schema = "dbo")
public class Disco {

    @Id
    @Column(name = "id_disco")
    private Integer idDisco;

    @ManyToOne
    @JoinColumn(name = "id_modelo_disco", nullable = false)
    private DiscoModelo modeloDisco;

    @Column(name = "numero_serie", length = 50)
    private String numeroSerie;

    @Column(name = "fecha_compra")
    private LocalDate fechaCompra;

    @Column(name = "estado_salud", length = 30)
    private String estadoSalud;

    @ManyToOne
    @JoinColumn(name = "id_activo")
    private Activo activo;
}
