package soportec.demo.services.service;

import java.util.List;
import java.util.Optional;
import soportec.demo.models.Activo;

public interface ActivoService {

    List<Activo> findAll();

    Optional<Activo> findById(Integer id);

    Activo save(Activo entity);

    void deleteById(Integer id);
}
