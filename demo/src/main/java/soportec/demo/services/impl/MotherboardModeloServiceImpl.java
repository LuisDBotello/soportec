package soportec.demo.services.impl;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import soportec.demo.models.MotherboardModelo;
import soportec.demo.repositories.MotherboardModeloRepository;
import soportec.demo.services.service.MotherboardModeloService;

@Service
public class MotherboardModeloServiceImpl implements MotherboardModeloService {

    private final MotherboardModeloRepository repository;

    public MotherboardModeloServiceImpl(MotherboardModeloRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<MotherboardModelo> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<MotherboardModelo> findById(Integer id) {
        return repository.findById(id);
    }

    @Override
    public MotherboardModelo save(MotherboardModelo entity) {
        return repository.save(entity);
    }

    @Override
    public void deleteById(Integer id) {
        repository.deleteById(id);
    }
}
