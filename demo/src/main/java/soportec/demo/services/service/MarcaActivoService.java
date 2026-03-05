package soportec.demo.services.service;

import java.util.List;
import java.util.Optional;
import soportec.demo.models.MarcaActivo;

public interface MarcaActivoService {

    List<MarcaActivo> findAll();

    Optional<MarcaActivo> findById(Integer id);

    MarcaActivo save(MarcaActivo entity);

    void deleteById(Integer id);

    List<MarcaActivo> findByTipoActivo(Integer idTipoActivo);
}
