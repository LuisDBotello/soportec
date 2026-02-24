package soportec.demo.services.service;

import java.util.List;
import java.util.Optional;
import soportec.demo.models.DiscoModelo;

public interface DiscoModeloService {

    List<DiscoModelo> findAll();

    Optional<DiscoModelo> findById(Integer id);

    DiscoModelo save(DiscoModelo entity);

    void deleteById(Integer id);
}
