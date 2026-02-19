package soportec.demo.services;

import java.util.List;
import java.util.Optional;

import soportec.demo.models.OrdenEvento;
import soportec.demo.models.OrdenEventoId;

public interface OrdenEventoService {

    List<OrdenEvento> findAll();

    Optional<OrdenEvento> findById(OrdenEventoId id);

    OrdenEvento save(OrdenEvento entity);

    void deleteById(OrdenEventoId id);
}
