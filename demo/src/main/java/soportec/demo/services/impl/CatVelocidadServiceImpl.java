package soportec.demo.services.impl;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import soportec.demo.models.CatVelocidad;
import soportec.demo.repositories.CatVelocidadRepository;
import soportec.demo.services.service.CatVelocidadService;

@Service
public class CatVelocidadServiceImpl implements CatVelocidadService {

    private final CatVelocidadRepository repository;

    public CatVelocidadServiceImpl(CatVelocidadRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<CatVelocidad> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<CatVelocidad> findById(Integer id) {
        return repository.findById(id);
    }

    @Override
    public CatVelocidad save(CatVelocidad entity) {
        return repository.save(entity);
    }

    @Override
    public void deleteById(Integer id) {
        repository.deleteById(id);
    }
}
