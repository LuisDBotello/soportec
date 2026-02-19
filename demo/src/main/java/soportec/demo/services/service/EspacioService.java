package soportec.demo.services.service;

import java.util.List;
import java.util.Optional;
import soportec.demo.models.Espacio;

public interface EspacioService {

    List<Espacio> findAll();

    Optional<Espacio> findById(Integer id);

    Espacio save(Espacio entity);

    void deleteById(Integer id);
}
