package soportec.demo.services.impl;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import soportec.demo.models.Marca;
import soportec.demo.repositories.MarcaRepository;
import soportec.demo.services.service.MarcaService;

@Service
public class MarcaServiceImpl implements MarcaService {

    private final MarcaRepository repository;

    public MarcaServiceImpl(MarcaRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Marca> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<Marca> findById(Integer id) {
        return repository.findById(id);
    }

    @Override
    public Marca save(Marca entity) {
        return repository.save(entity);
    }

    @Override
    public void deleteById(Integer id) {
        repository.deleteById(id);
    }
}
