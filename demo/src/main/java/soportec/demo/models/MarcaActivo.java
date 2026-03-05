package soportec.demo.models;

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
@Table(name = "MARCA_ACTIVO", schema = "dbo")
public class MarcaActivo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_marca_activo")
    private Integer idMarcaActivo;

    @ManyToOne
    @JoinColumn(name = "id_tipo_activo", nullable = false)
    private TipoActivo tipoActivo;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;
}
