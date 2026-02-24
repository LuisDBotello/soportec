package soportec.demo.services.impl;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import soportec.demo.models.CatActivo;
import soportec.demo.repositories.CatActivoRepository;
import soportec.demo.services.service.CatActivoService;

@Service
public class CatActivoServiceImpl implements CatActivoService {

    private final CatActivoRepository repository;

    public CatActivoServiceImpl(CatActivoRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<CatActivo> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<CatActivo> findById(Integer id) {
        return repository.findById(id);
    }

    @Override
    public CatActivo save(CatActivo entity) {
        return repository.save(entity);
    }

    @Override
    public void deleteById(Integer id) {
        repository.deleteById(id);
    }
}
