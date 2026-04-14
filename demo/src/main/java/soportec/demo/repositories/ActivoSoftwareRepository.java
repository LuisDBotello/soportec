package soportec.demo.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import soportec.demo.models.ActivoSoftware;
import soportec.demo.models.Software;
import soportec.demo.models.ids.ActivoSoftwareId;

public interface ActivoSoftwareRepository extends JpaRepository<ActivoSoftware, ActivoSoftwareId> {

    boolean existsByActivo_IdActivoAndSoftware_IdSoftware(Integer idActivo, Integer idSoftware);

    void deleteByActivo_IdActivoAndSoftware_IdSoftware(Integer idActivo, Integer idSoftware);

    @Query("SELECT a.software FROM ActivoSoftware a WHERE a.activo.idActivo = :idActivo ORDER BY a.software.nombre ASC")
    List<Software> findSoftwareByActivo(@Param("idActivo") Integer idActivo);
}
