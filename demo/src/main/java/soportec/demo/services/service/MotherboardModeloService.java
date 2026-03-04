package soportec.demo.services.service;

import java.util.List;
import java.util.Optional;
import soportec.demo.models.MotherboardModelo;

public interface MotherboardModeloService {

    List<MotherboardModelo> findAll();

    Optional<MotherboardModelo> findById(Integer id);

    MotherboardModelo save(MotherboardModelo entity);

    void deleteById(Integer id);

    List<MotherboardModelo> findByMarcaMotherboard(Integer idMarcaMotherboard);
}
