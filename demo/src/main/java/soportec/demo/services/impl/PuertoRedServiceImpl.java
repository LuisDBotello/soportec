package soportec.demo.services.impl;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import soportec.demo.models.PuertoRed;
import soportec.demo.repositories.PuertoRedRepository;
import soportec.demo.services.service.PuertoRedService;

@Service
public class PuertoRedServiceImpl implements PuertoRedService {

    private final PuertoRedRepository repository;

    public PuertoRedServiceImpl(PuertoRedRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<PuertoRed> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<PuertoRed> findById(Integer id) {
        return repository.findById(id);
    }

    @Override
    public PuertoRed save(PuertoRed entity) {
        return repository.save(entity);
    }

    @Override
    public void deleteById(Integer id) {
        repository.deleteById(id);
    }
}
