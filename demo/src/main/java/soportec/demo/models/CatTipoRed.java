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
@Table(name = "CAT_TIPO_RED", schema = "dbo")
public class CatTipoRed {

    @Id
    @Column(name = "id_tipo_red")
    private Integer idTipoRed;

    @Column(name = "nombre", nullable = false, length = 30)
    private String nombre;
}
