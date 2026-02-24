package soportec.demo.services.impl;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import soportec.demo.models.EquipoRed;
import soportec.demo.repositories.EquipoRedRepository;
import soportec.demo.services.service.EquipoRedService;

@Service
public class EquipoRedServiceImpl implements EquipoRedService {

    private final EquipoRedRepository repository;

    public EquipoRedServiceImpl(EquipoRedRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<EquipoRed> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<EquipoRed> findById(Integer id) {
        return repository.findById(id);
    }

    @Override
    public EquipoRed save(EquipoRed entity) {
        return repository.save(entity);
    }

    @Override
    public void deleteById(Integer id) {
        repository.deleteById(id);
    }
}
