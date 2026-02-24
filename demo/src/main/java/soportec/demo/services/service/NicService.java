package soportec.demo.services.service;

import java.util.List;
import java.util.Optional;
import soportec.demo.models.Nic;

public interface NicService {

    List<Nic> findAll();

    Optional<Nic> findById(Integer id);

    Nic save(Nic entity);

    void deleteById(Integer id);
}
