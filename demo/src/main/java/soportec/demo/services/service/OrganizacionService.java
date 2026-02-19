package soportec.demo.services.service;

import java.util.List;
import java.util.Optional;
import soportec.demo.models.Organizacion;

public interface OrganizacionService {

    List<Organizacion> findAll();

    Optional<Organizacion> findById(Integer id);

    Organizacion save(Organizacion entity);

    void deleteById(Integer id);
}
