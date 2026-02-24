package soportec.demo.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
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
@Table(name = "MOTHERBOARD_MARCA", schema = "dbo")
public class MotherboardMarca {

    @Id
    @Column(name = "id_marca_motherboard")
    private Integer idMarcaMotherboard;

    @Column(name = "nombre", nullable = false, length = 50)
    private String nombre;
}
