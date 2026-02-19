package soportec.demo.services.impl;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import soportec.demo.models.EstadoEvento;
import soportec.demo.repositories.EstadoEventoRepository;
import soportec.demo.services.service.EstadoEventoService;

@Service
public class EstadoEventoServiceImpl implements EstadoEventoService {

    private final EstadoEventoRepository repository;

    public EstadoEventoServiceImpl(EstadoEventoRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<EstadoEvento> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<EstadoEvento> findById(Integer id) {
        return repository.findById(id);
    }

    @Override
    public EstadoEvento save(EstadoEvento entity) {
        return repository.save(entity);
    }

    @Override
    public void deleteById(Integer id) {
        repository.deleteById(id);
    }
}
