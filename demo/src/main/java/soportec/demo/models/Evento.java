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
@Table(name = "EVENTO")
public class Evento {

    @Id
    @Column(name = "id")
    private Integer id;

    @Column(name = "clave", nullable = false, length = 50)
    private String clave;

    @Column(name = "descripcion", nullable = true, length = 255)
    private String descripcion;
}