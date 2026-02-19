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
@Table(name = "MARCA")
public class Marca {
    
    @Id
    @Column(name = "id_marca")
    private Integer id_marca;

    @Column(name = "nombre", nullable = false, length = 50)
    private String nombre;
}
