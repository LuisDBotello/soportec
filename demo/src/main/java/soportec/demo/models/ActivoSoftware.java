package soportec.demo.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import soportec.demo.models.ids.ActivoSoftwareId;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@IdClass(ActivoSoftwareId.class)
@Table(name = "ACTIVO_SOFTWARE", schema = "dbo")
public class ActivoSoftware {

    @Id
    @ManyToOne
    @JoinColumn(name = "id_activo", referencedColumnName = "id_activo", nullable = false)
    private Activo activo;

    @Id
    @ManyToOne
    @JoinColumn(name = "id_software", referencedColumnName = "id_software", nullable = false)
    private Software software;
}
