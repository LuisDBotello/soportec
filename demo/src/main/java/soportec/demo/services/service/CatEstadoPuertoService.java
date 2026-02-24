package soportec.demo.services.service;

import java.util.List;
import java.util.Optional;
import soportec.demo.models.CatEstadoPuerto;

public interface CatEstadoPuertoService {

    List<CatEstadoPuerto> findAll();

    Optional<CatEstadoPuerto> findById(Integer id);

    CatEstadoPuerto save(CatEstadoPuerto entity);

    void deleteById(Integer id);
}
