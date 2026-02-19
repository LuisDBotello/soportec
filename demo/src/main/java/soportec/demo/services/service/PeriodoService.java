package soportec.demo.services.service;

import java.util.List;
import java.util.Optional;
import soportec.demo.models.Periodo;

public interface PeriodoService {

    List<Periodo> findAll();

    Optional<Periodo> findById(Integer id);

    Periodo save(Periodo entity);

    void deleteById(Integer id);
}
