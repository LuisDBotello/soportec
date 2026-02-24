package soportec.demo.services.service;

import java.util.List;
import java.util.Optional;
import soportec.demo.models.CatActivo;

public interface CatActivoService {

    List<CatActivo> findAll();

    Optional<CatActivo> findById(Integer id);

    CatActivo save(CatActivo entity);

    void deleteById(Integer id);
}
