package soportec.demo.services.impl;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import soportec.demo.models.Ram;
import soportec.demo.repositories.RamRepository;
import soportec.demo.services.service.RamService;

@Service
public class RamServiceImpl implements RamService {

    private final RamRepository repository;

    public RamServiceImpl(RamRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Ram> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<Ram> findById(Integer id) {
        return repository.findById(id);
    }

    @Override
    public Ram save(Ram entity) {
        return repository.save(entity);
    }

    @Override
    public void deleteById(Integer id) {
        repository.deleteById(id);
    }

    @Override
    public List<Ram> findDisponibles() {
        return repository.findByActivoIsNull();
    }

    @Override
    public Optional<Ram> findDisponibleById(Integer id) {
        return repository.findByIdRamAndActivoIsNull(id);
    }
}
