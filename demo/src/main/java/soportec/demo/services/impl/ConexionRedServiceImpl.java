package soportec.demo.services.impl;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import soportec.demo.models.ConexionRed;
import soportec.demo.repositories.ConexionRedRepository;
import soportec.demo.services.service.ConexionRedService;

@Service
public class ConexionRedServiceImpl implements ConexionRedService {

    private final ConexionRedRepository repository;

    public ConexionRedServiceImpl(ConexionRedRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<ConexionRed> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<ConexionRed> findById(Integer id) {
        return repository.findById(id);
    }

    @Override
    public ConexionRed save(ConexionRed entity) {
        return repository.save(entity);
    }

    @Override
    public void deleteById(Integer id) {
        repository.deleteById(id);
    }
}
