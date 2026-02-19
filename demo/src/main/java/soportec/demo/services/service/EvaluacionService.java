package soportec.demo.services.service;

import java.util.List;
import java.util.Optional;
import soportec.demo.models.Evaluacion;

public interface EvaluacionService {

    List<Evaluacion> findAll();

    Optional<Evaluacion> findById(Integer id);

    Evaluacion save(Evaluacion entity);

    void deleteById(Integer id);
}
