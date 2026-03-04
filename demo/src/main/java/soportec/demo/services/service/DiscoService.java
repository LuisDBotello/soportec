package soportec.demo.services.service;

import java.util.List;
import java.util.Optional;
import soportec.demo.models.Disco;

public interface DiscoService {

    List<Disco> findAll();

    Optional<Disco> findById(Integer id);

    Disco save(Disco entity);

    void deleteById(Integer id);

    List<Disco> findDisponibles();

    Optional<Disco> findDisponibleById(Integer id);
}
