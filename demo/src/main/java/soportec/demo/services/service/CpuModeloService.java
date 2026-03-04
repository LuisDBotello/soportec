package soportec.demo.services.service;

import java.util.List;
import java.util.Optional;
import soportec.demo.models.CpuModelo;

public interface CpuModeloService {

    List<CpuModelo> findAll();

    Optional<CpuModelo> findById(Integer id);

    CpuModelo save(CpuModelo entity);

    void deleteById(Integer id);

    List<CpuModelo> findByMarcaCpu(Integer idMarcaCpu);
}
