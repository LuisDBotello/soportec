package soportec.demo.services.impl;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import soportec.demo.models.Evaluacion;
import soportec.demo.repositories.EvaluacionRepository;
import soportec.demo.services.service.EvaluacionService;

@Service
public class EvaluacionServiceImpl implements EvaluacionService {

    private final EvaluacionRepository repository;

    public EvaluacionServiceImpl(EvaluacionRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Evaluacion> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<Evaluacion> findById(Integer id) {
        return repository.findById(id);
    }

    @Override
    public Evaluacion save(Evaluacion entity) {
        return repository.save(entity);
    }

    @Override
    public void deleteById(Integer id) {
        repository.deleteById(id);
    }
}
