package soportec.demo.services.service;

import java.util.List;
import java.util.Optional;
import soportec.demo.models.CatVelocidad;

public interface CatVelocidadService {

    List<CatVelocidad> findAll();

    Optional<CatVelocidad> findById(Integer id);

    CatVelocidad save(CatVelocidad entity);

    void deleteById(Integer id);
}
