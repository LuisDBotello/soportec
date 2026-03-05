package soportec.demo.services.impl;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import soportec.demo.models.Disco;
import soportec.demo.repositories.DiscoRepository;
import soportec.demo.services.service.DiscoService;

@Service
public class DiscoServiceImpl implements DiscoService {

    private final DiscoRepository repository;

    public DiscoServiceImpl(DiscoRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Disco> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<Disco> findById(Integer id) {
        return repository.findById(id);
    }

    @Override
    public Disco save(Disco entity) {
        return repository.save(entity);
    }

    @Override
    public void deleteById(Integer id) {
        repository.deleteById(id);
    }

    @Override
    public List<Disco> findDisponibles() {
        return repository.findByActivoIsNull();
    }

    @Override
    public Optional<Disco> findDisponibleById(Integer id) {
        return repository.findByIdDiscoAndActivoIsNull(id);
    }

    public Integer getNextIdDisco() {
        return repository.findMaxIdDisco() + 1;
    }
}
