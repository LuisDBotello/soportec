package soportec.demo.services.service;

import java.util.List;
import java.util.Optional;
import soportec.demo.models.NicModelo;

public interface NicModeloService {

    List<NicModelo> findAll();

    Optional<NicModelo> findById(Integer id);

    NicModelo save(NicModelo entity);

    void deleteById(Integer id);

    List<NicModelo> findByMarcaNic(Integer idMarcaNic);
}
