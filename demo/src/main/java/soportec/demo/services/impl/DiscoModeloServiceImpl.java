package soportec.demo.services.impl;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import soportec.demo.models.DiscoModelo;
import soportec.demo.repositories.DiscoModeloRepository;
import soportec.demo.services.service.DiscoModeloService;

@Service
public class DiscoModeloServiceImpl implements DiscoModeloService {

    private final DiscoModeloRepository repository;

    public DiscoModeloServiceImpl(DiscoModeloRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<DiscoModelo> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<DiscoModelo> findById(Integer id) {
        return repository.findById(id);
    }

    @Override
    public DiscoModelo save(DiscoModelo entity) {
        return repository.save(entity);
    }

    @Override
    public void deleteById(Integer id) {
        repository.deleteById(id);
    }
}
