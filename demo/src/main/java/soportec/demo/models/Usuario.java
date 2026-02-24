package soportec.demo.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "usuario", schema = "dbo")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_USUARIO")
    private Integer idUsuario;

    @Column(name = "NOMBRE", nullable = false, length = 120)
    private String nombre;

    @Column(name = "APELLIDO_P", nullable = false, length = 50)
    private String apellidoP;

    @Column(name = "APELLIDO_M", length = 50)
    private String apellidoM;

    @Column(name = "CORREO", length = 100)
    private String correo;

    @Column(name = "USERNAME", length = 50)
    private String username;

    @Column(name = "PASSWORDHASH", length = 255)
    private String passwordHash;

    @ManyToOne
    @JoinColumn(name = "id_depto", nullable = false)
    private Departamento departamento;

    @ManyToOne
    @JoinColumn(name = "id_nivel", nullable = false)
    private NivelPriv nivel;
}
