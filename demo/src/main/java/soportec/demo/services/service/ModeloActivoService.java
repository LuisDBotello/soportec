package soportec.demo.services.service;

import java.util.List;
import java.util.Optional;
import soportec.demo.models.ModeloActivo;

public interface ModeloActivoService {

    List<ModeloActivo> findAll();

    Optional<ModeloActivo> findById(Integer id);

    ModeloActivo save(ModeloActivo entity);

    void deleteById(Integer id);

    List<ModeloActivo> findByMarcaActivo(Integer idMarcaActivo);
}
