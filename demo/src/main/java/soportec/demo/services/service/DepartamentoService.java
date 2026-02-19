package soportec.demo.services.service;

import java.util.List;
import java.util.Optional;
import soportec.demo.models.Departamento;

public interface DepartamentoService {

    List<Departamento> findAll();

    Optional<Departamento> findById(Integer id);

    Departamento save(Departamento entity);

    void deleteById(Integer id);
}
