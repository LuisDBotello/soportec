package soportec.demo.services.impl;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import soportec.demo.models.Cpu;
import soportec.demo.repositories.CpuRepository;
import soportec.demo.services.service.CpuService;

@Service
public class CpuServiceImpl implements CpuService {

    private final CpuRepository repository;

    public CpuServiceImpl(CpuRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Cpu> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<Cpu> findById(Integer id) {
        return repository.findById(id);
    }

    @Override
    public Cpu save(Cpu entity) {
        return repository.save(entity);
    }

    @Override
    public void deleteById(Integer id) {
        repository.deleteById(id);
    }
}
