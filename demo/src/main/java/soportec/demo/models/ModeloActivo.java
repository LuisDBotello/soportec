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
@Table(name = "MODELO_ACTIVO", schema = "dbo")
public class ModeloActivo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_modelo_activo")
    private Integer idModeloActivo;

    @ManyToOne
    @JoinColumn(name = "id_marca_activo", nullable = false)
    private MarcaActivo marcaActivo;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;
}
