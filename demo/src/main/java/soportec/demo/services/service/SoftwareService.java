package soportec.demo.services.service;

import java.util.List;
import java.util.Optional;
import soportec.demo.models.Software;

public interface SoftwareService {

    List<Software> findAll();

    Optional<Software> findById(Integer id);

    Software save(Software entity);

    void deleteById(Integer id);
}
