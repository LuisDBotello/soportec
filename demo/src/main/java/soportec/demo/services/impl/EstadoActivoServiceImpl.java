package soportec.demo.services.impl;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import soportec.demo.models.EstadoActivo;
import soportec.demo.repositories.EstadoActivoRepository;
import soportec.demo.services.service.EstadoActivoService;

@Service
public class EstadoActivoServiceImpl implements EstadoActivoService {

    private final EstadoActivoRepository repository;

    public EstadoActivoServiceImpl(EstadoActivoRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<EstadoActivo> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<EstadoActivo> findById(Integer id) {
        return repository.findById(id);
    }

    @Override
    public EstadoActivo save(EstadoActivo entity) {
        return repository.save(entity);
    }

    @Override
    public void deleteById(Integer id) {
        repository.deleteById(id);
    }
}
