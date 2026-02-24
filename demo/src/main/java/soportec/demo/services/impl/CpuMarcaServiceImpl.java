package soportec.demo.services.impl;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import soportec.demo.models.CpuMarca;
import soportec.demo.repositories.CpuMarcaRepository;
import soportec.demo.services.service.CpuMarcaService;

@Service
public class CpuMarcaServiceImpl implements CpuMarcaService {

    private final CpuMarcaRepository repository;

    public CpuMarcaServiceImpl(CpuMarcaRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<CpuMarca> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<CpuMarca> findById(Integer id) {
        return repository.findById(id);
    }

    @Override
    public CpuMarca save(CpuMarca entity) {
        return repository.save(entity);
    }

    @Override
    public void deleteById(Integer id) {
        repository.deleteById(id);
    }
}
