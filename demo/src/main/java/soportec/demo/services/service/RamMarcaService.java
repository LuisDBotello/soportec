package soportec.demo.services.service;

import java.util.List;
import java.util.Optional;
import soportec.demo.models.RamMarca;

public interface RamMarcaService {

    List<RamMarca> findAll();

    Optional<RamMarca> findById(Integer id);

    RamMarca save(RamMarca entity);

    void deleteById(Integer id);
}
