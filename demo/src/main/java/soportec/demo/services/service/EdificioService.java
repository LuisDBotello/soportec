package soportec.demo.services.service;

import java.util.List;
import java.util.Optional;
import soportec.demo.models.Edificio;

public interface EdificioService {

    List<Edificio> findAll();

    Optional<Edificio> findById(Integer id);

    Edificio save(Edificio entity);

    void deleteById(Integer id);
}
