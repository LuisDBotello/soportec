package soportec.demo.services.service;

import java.util.List;
import java.util.Optional;
import soportec.demo.models.DiscoMarca;

public interface DiscoMarcaService {

    List<DiscoMarca> findAll();

    Optional<DiscoMarca> findById(Integer id);

    DiscoMarca save(DiscoMarca entity);

    void deleteById(Integer id);
}
