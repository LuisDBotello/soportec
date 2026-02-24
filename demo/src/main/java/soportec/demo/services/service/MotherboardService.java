package soportec.demo.services.service;

import java.util.List;
import java.util.Optional;
import soportec.demo.models.Motherboard;

public interface MotherboardService {

    List<Motherboard> findAll();

    Optional<Motherboard> findById(Integer id);

    Motherboard save(Motherboard entity);

    void deleteById(Integer id);
}
