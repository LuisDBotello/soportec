package soportec.demo.services.service;

import java.util.List;
import java.util.Optional;
import soportec.demo.models.Cpu;

public interface CpuService {

    List<Cpu> findAll();

    Optional<Cpu> findById(Integer id);

    Cpu save(Cpu entity);

    void deleteById(Integer id);
}
