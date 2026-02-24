package soportec.demo.services.service;

import java.util.List;
import java.util.Optional;
import soportec.demo.models.RamModelo;

public interface RamModeloService {

    List<RamModelo> findAll();

    Optional<RamModelo> findById(Integer id);

    RamModelo save(RamModelo entity);

    void deleteById(Integer id);
}
