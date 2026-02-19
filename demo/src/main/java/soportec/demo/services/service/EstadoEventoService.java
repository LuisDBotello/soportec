package soportec.demo.services.service;

import java.util.List;
import java.util.Optional;
import soportec.demo.models.EstadoEvento;

public interface EstadoEventoService {

    List<EstadoEvento> findAll();

    Optional<EstadoEvento> findById(Integer id);

    EstadoEvento save(EstadoEvento entity);

    void deleteById(Integer id);
}
