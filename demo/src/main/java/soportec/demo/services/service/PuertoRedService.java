package soportec.demo.services.service;

import java.util.List;
import java.util.Optional;
import soportec.demo.models.PuertoRed;

public interface PuertoRedService {

    List<PuertoRed> findAll();

    Optional<PuertoRed> findById(Integer id);

    PuertoRed save(PuertoRed entity);

    void deleteById(Integer id);
}
