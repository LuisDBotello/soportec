package soportec.demo.services.service;

import java.util.List;
import java.util.Optional;
import soportec.demo.models.TipoActivo;

public interface TipoActivoService {

    List<TipoActivo> findAll();

    Optional<TipoActivo> findById(Integer id);

    TipoActivo save(TipoActivo entity);

    void deleteById(Integer id);

    Boolean esEscritorio(TipoActivo tipoActivo);

    List<TipoActivo> findByCategoriaActivo(Integer idCategoriaActivo);
}
