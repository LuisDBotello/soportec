package soportec.demo.services.impl;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import soportec.demo.models.MotherboardMarca;
import soportec.demo.repositories.MotherboardMarcaRepository;
import soportec.demo.services.service.MotherboardMarcaService;

@Service
public class MotherboardMarcaServiceImpl implements MotherboardMarcaService {

    private final MotherboardMarcaRepository repository;

    public MotherboardMarcaServiceImpl(MotherboardMarcaRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<MotherboardMarca> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<MotherboardMarca> findById(Integer id) {
        return repository.findById(id);
    }

    @Override
    public MotherboardMarca save(MotherboardMarca entity) {
        return repository.save(entity);
    }

    @Override
    public void deleteById(Integer id) {
        repository.deleteById(id);
    }
}
