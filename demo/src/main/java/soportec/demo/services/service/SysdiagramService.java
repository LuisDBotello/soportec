package soportec.demo.services.service;

import java.util.List;
import java.util.Optional;
import soportec.demo.models.Sysdiagram;

public interface SysdiagramService {

    List<Sysdiagram> findAll();

    Optional<Sysdiagram> findById(Integer id);

    Sysdiagram save(Sysdiagram entity);

    void deleteById(Integer id);
}
