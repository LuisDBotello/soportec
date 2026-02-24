package soportec.demo.services.service;

import java.util.List;
import java.util.Optional;
import soportec.demo.models.ConexionRed;

public interface ConexionRedService {

    List<ConexionRed> findAll();

    Optional<ConexionRed> findById(Integer id);

    ConexionRed save(ConexionRed entity);

    void deleteById(Integer id);
}
