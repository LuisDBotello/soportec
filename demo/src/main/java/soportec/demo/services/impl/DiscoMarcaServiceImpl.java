package soportec.demo.services.impl;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import soportec.demo.models.DiscoMarca;
import soportec.demo.repositories.DiscoMarcaRepository;
import soportec.demo.services.service.DiscoMarcaService;

@Service
public class DiscoMarcaServiceImpl implements DiscoMarcaService {

    private final DiscoMarcaRepository repository;

    public DiscoMarcaServiceImpl(DiscoMarcaRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<DiscoMarca> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<DiscoMarca> findById(Integer id) {
        return repository.findById(id);
    }

    @Override
    public DiscoMarca save(DiscoMarca entity) {
        return repository.save(entity);
    }

    @Override
    public void deleteById(Integer id) {
        repository.deleteById(id);
    }
}
