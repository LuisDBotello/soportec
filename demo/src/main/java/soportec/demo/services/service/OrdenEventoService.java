package soportec.demo.services.service;

import java.util.List;
import java.util.Optional;
import java.io.Serializable;
import soportec.demo.models.OrdenEvento;

public interface OrdenEventoService {

    List<OrdenEvento> findAll();

    Optional<OrdenEvento> findById(Serializable id);

    OrdenEvento save(OrdenEvento entity);

    void deleteById(Serializable id);
}
