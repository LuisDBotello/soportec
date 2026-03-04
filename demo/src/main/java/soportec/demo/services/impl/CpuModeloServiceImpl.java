package soportec.demo.services.impl;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import soportec.demo.models.CpuModelo;
import soportec.demo.repositories.CpuModeloRepository;
import soportec.demo.services.service.CpuModeloService;

@Service
public class CpuModeloServiceImpl implements CpuModeloService {

    private final CpuModeloRepository repository;

    public CpuModeloServiceImpl(CpuModeloRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<CpuModelo> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<CpuModelo> findById(Integer id) {
        return repository.findById(id);
    }

    @Override
    public CpuModelo save(CpuModelo entity) {
        return repository.save(entity);
    }

    @Override
    public void deleteById(Integer id) {
        repository.deleteById(id);
    }

    @Override
    public List<CpuModelo> findByMarcaCpu(Integer idMarcaCpu) {
        return repository.findByMarcaCpu_IdMarcaCpu(idMarcaCpu);
    }
}
