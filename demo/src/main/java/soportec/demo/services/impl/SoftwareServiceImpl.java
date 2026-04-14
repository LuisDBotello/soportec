package soportec.demo.services.impl;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import soportec.demo.models.Software;
import soportec.demo.repositories.SoftwareRepository;
import soportec.demo.services.service.SoftwareService;

@Service
public class SoftwareServiceImpl implements SoftwareService {

    private final SoftwareRepository repository;

    public SoftwareServiceImpl(SoftwareRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Software> findAll() {
        return repository.findAllByOrderByNombreAsc();
    }

    @Override
    public Optional<Software> findById(Integer id) {
        return repository.findById(id);
    }

    @Override
    public Software save(Software entity) {
        return repository.save(entity);
    }

    @Override
    public void deleteById(Integer id) {
        repository.deleteById(id);
    }
}
