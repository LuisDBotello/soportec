package soportec.demo.services.service;

import java.util.List;
import java.util.Optional;
import soportec.demo.models.Marca;

public interface MarcaService {

    List<Marca> findAll();

    Optional<Marca> findById(Integer id);

    Marca save(Marca entity);

    void deleteById(Integer id);
}
