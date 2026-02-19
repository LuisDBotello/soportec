package soportec.demo.services.impl;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import soportec.demo.models.Periodo;
import soportec.demo.repositories.PeriodoRepository;
import soportec.demo.services.service.PeriodoService;

@Service
public class PeriodoServiceImpl implements PeriodoService {

    private final PeriodoRepository repository;

    public PeriodoServiceImpl(PeriodoRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Periodo> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<Periodo> findById(Integer id) {
        return repository.findById(id);
    }

    @Override
    public Periodo save(Periodo entity) {
        return repository.save(entity);
    }

    @Override
    public void deleteById(Integer id) {
        repository.deleteById(id);
    }
}
