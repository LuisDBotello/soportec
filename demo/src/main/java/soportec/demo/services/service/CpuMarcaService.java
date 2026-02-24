package soportec.demo.services.service;

import java.util.List;
import java.util.Optional;
import soportec.demo.models.CpuMarca;

public interface CpuMarcaService {

    List<CpuMarca> findAll();

    Optional<CpuMarca> findById(Integer id);

    CpuMarca save(CpuMarca entity);

    void deleteById(Integer id);
}
