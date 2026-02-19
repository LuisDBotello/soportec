package soportec.demo.services.impl;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import soportec.demo.models.Equipo;
import soportec.demo.repositories.EquipoRepository;
import soportec.demo.services.service.EquipoService;

@Service
public class EquipoServiceImpl implements EquipoService {

    private final EquipoRepository repository;

    public EquipoServiceImpl(EquipoRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Equipo> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<Equipo> findById(Integer id) {
        return repository.findById(id);
    }

    @Override
    public Equipo save(Equipo entity) {
        return repository.save(entity);
    }

    @Override
    public void deleteById(Integer id) {
        repository.deleteById(id);
    }
}
