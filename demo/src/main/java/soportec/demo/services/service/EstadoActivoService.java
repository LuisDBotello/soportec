package soportec.demo.services.service;

import java.util.List;
import java.util.Optional;
import soportec.demo.models.EstadoActivo;

public interface EstadoActivoService {

    List<EstadoActivo> findAll();

    Optional<EstadoActivo> findById(Integer id);

    EstadoActivo save(EstadoActivo entity);

    void deleteById(Integer id);
}
