package soportec.demo.services.service;

import java.util.List;
import java.util.Optional;
import soportec.demo.models.Evento;

public interface EventoService {

    List<Evento> findAll();

    Optional<Evento> findById(Integer id);

    Evento save(Evento entity);

    void deleteById(Integer id);

    Integer getNextId();
}
