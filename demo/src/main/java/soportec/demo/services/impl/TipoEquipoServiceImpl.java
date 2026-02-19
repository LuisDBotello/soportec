package soportec.demo.services.impl;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import soportec.demo.models.TipoEquipo;
import soportec.demo.repositories.TipoEquipoRepository;
import soportec.demo.services.service.TipoEquipoService;

@Service
public class TipoEquipoServiceImpl implements TipoEquipoService {

    private final TipoEquipoRepository repository;

    public TipoEquipoServiceImpl(TipoEquipoRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<TipoEquipo> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<TipoEquipo> findById(Integer id) {
        return repository.findById(id);
    }

    @Override
    public TipoEquipo save(TipoEquipo entity) {
        return repository.save(entity);
    }

    @Override
    public void deleteById(Integer id) {
        repository.deleteById(id);
    }
}
