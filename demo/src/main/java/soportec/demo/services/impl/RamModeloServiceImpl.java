package soportec.demo.services.impl;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import soportec.demo.models.RamModelo;
import soportec.demo.repositories.RamModeloRepository;
import soportec.demo.services.service.RamModeloService;

@Service
public class RamModeloServiceImpl implements RamModeloService {

    private final RamModeloRepository repository;

    public RamModeloServiceImpl(RamModeloRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<RamModelo> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<RamModelo> findById(Integer id) {
        return repository.findById(id);
    }

    @Override
    public RamModelo save(RamModelo entity) {
        return repository.save(entity);
    }

    @Override
    public void deleteById(Integer id) {
        repository.deleteById(id);
    }
}
