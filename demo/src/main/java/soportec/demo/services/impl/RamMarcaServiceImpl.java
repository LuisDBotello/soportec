package soportec.demo.services.impl;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import soportec.demo.models.RamMarca;
import soportec.demo.repositories.RamMarcaRepository;
import soportec.demo.services.service.RamMarcaService;

@Service
public class RamMarcaServiceImpl implements RamMarcaService {

    private final RamMarcaRepository repository;

    public RamMarcaServiceImpl(RamMarcaRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<RamMarca> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<RamMarca> findById(Integer id) {
        return repository.findById(id);
    }

    @Override
    public RamMarca save(RamMarca entity) {
        return repository.save(entity);
    }

    @Override
    public void deleteById(Integer id) {
        repository.deleteById(id);
    }
}
