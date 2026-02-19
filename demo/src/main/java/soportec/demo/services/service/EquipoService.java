package soportec.demo.services.service;

import java.util.List;
import java.util.Optional;
import soportec.demo.models.Equipo;

public interface EquipoService {

    List<Equipo> findAll();

    Optional<Equipo> findById(Integer id);

    Equipo save(Equipo entity);

    void deleteById(Integer id);
}
