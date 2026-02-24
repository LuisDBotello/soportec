package soportec.demo.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
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
@Table(name = "sysdiagrams", schema = "dbo")
public class Sysdiagram {

    @Id
    @Column(name = "diagram_id")
    private Integer diagramId;

    @Column(name = "name", nullable = false, length = 256)
    private String name;

    @Column(name = "principal_id", nullable = false)
    private Integer principalId;

    @Column(name = "version")
    private Integer version;

    @Lob
    @Column(name = "definition")
    private byte[] definition;
}
