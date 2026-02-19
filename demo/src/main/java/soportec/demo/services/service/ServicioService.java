package soportec.demo.services.service;

import java.util.List;
import java.util.Optional;
import soportec.demo.models.Servicio;

public interface ServicioService {

    List<Servicio> findAll();

    Optional<Servicio> findById(Integer id);

    Servicio save(Servicio entity);

    void deleteById(Integer id);
}
