package soportec.demo.services.service;

import java.util.List;
import java.util.Optional;
import soportec.demo.models.TipoEquipo;

public interface TipoEquipoService {

    List<TipoEquipo> findAll();

    Optional<TipoEquipo> findById(Integer id);

    TipoEquipo save(TipoEquipo entity);

    void deleteById(Integer id);
}
