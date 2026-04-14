package soportec.demo.services.service;

import java.util.List;
import java.util.Optional;
import soportec.demo.models.ActivoSoftware;
import soportec.demo.models.Software;
import soportec.demo.models.ids.ActivoSoftwareId;

public interface ActivoSoftwareService {

    List<ActivoSoftware> findAll();

    Optional<ActivoSoftware> findById(ActivoSoftwareId id);

    ActivoSoftware save(ActivoSoftware entity);

    void deleteById(ActivoSoftwareId id);

    boolean existsRelacion(Integer idActivo, Integer idSoftware);

    void deleteRelacion(Integer idActivo, Integer idSoftware);

    List<Software> findSoftwareByActivo(Integer idActivo);
}
