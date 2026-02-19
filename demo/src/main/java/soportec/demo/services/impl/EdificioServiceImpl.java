package soportec.demo.services.impl;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import soportec.demo.models.Edificio;
import soportec.demo.repositories.EdificioRepository;
import soportec.demo.services.service.EdificioService;

@Service
public class EdificioServiceImpl implements EdificioService {

    private final EdificioRepository repository;

    public EdificioServiceImpl(EdificioRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Edificio> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<Edificio> findById(Integer id) {
        return repository.findById(id);
    }

    @Override
    public Edificio save(Edificio entity) {
        return repository.save(entity);
    }

    @Override
    public void deleteById(Integer id) {
        repository.deleteById(id);
    }
}
