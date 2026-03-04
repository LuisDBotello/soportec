package soportec.demo.services.service;

import java.util.List;
import java.util.Optional;
import soportec.demo.models.Ram;

public interface RamService {

    List<Ram> findAll();

    Optional<Ram> findById(Integer id);

    Ram save(Ram entity);

    void deleteById(Integer id);

    List<Ram> findDisponibles();

    Optional<Ram> findDisponibleById(Integer id);
}
