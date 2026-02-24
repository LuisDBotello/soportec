package soportec.demo.services.impl;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import soportec.demo.models.NicMarca;
import soportec.demo.repositories.NicMarcaRepository;
import soportec.demo.services.service.NicMarcaService;

@Service
public class NicMarcaServiceImpl implements NicMarcaService {

    private final NicMarcaRepository repository;

    public NicMarcaServiceImpl(NicMarcaRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<NicMarca> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<NicMarca> findById(Integer id) {
        return repository.findById(id);
    }

    @Override
    public NicMarca save(NicMarca entity) {
        return repository.save(entity);
    }

    @Override
    public void deleteById(Integer id) {
        repository.deleteById(id);
    }
}
