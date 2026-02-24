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
@Table(name = "NIVEL_PRIV")
public class NivelPriv {
    
    @Id
    @Column(name = "id_nivel")
    private Integer id_nivel;

    @Column(name = "nombre", nullable = false, length = 20)
    private String nombre;
}
