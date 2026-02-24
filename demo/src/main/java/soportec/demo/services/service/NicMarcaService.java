package soportec.demo.services.service;

import java.util.List;
import java.util.Optional;
import soportec.demo.models.NicMarca;

public interface NicMarcaService {

    List<NicMarca> findAll();

    Optional<NicMarca> findById(Integer id);

    NicMarca save(NicMarca entity);

    void deleteById(Integer id);
}
