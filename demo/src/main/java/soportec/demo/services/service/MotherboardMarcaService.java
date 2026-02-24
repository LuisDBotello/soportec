package soportec.demo.services.service;

import java.util.List;
import java.util.Optional;
import soportec.demo.models.MotherboardMarca;

public interface MotherboardMarcaService {

    List<MotherboardMarca> findAll();

    Optional<MotherboardMarca> findById(Integer id);

    MotherboardMarca save(MotherboardMarca entity);

    void deleteById(Integer id);
}
