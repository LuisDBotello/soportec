package soportec.demo.services.service;

import java.util.List;
import java.util.Optional;
import soportec.demo.models.Orden;

public interface OrdenService {

    List<Orden> findAll();

    Optional<Orden> findById(Integer id);

    Orden save(Orden entity);

    void deleteById(Integer id);
}
