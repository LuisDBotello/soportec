package soportec.demo.services.service;

import java.util.List;
import java.util.Optional;
import soportec.demo.models.EquipoRed;

public interface EquipoRedService {

    List<EquipoRed> findAll();

    Optional<EquipoRed> findById(Integer id);

    EquipoRed save(EquipoRed entity);

    void deleteById(Integer id);
}
