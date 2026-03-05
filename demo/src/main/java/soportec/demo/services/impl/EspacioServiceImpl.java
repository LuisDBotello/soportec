package soportec.demo.services.impl;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import soportec.demo.models.Espacio;
import soportec.demo.repositories.EspacioRepository;
import soportec.demo.services.service.EspacioService;

@Service
public class EspacioServiceImpl implements EspacioService {

    private final EspacioRepository repository;

    public EspacioServiceImpl(EspacioRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Espacio> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<Espacio> findById(Integer id) {
        return repository.findById(id);
    }

    @Override
    public Espacio save(Espacio entity) {
        return repository.save(entity);
    }

    @Override
    public void deleteById(Integer id) {
        repository.deleteById(id);
    }

    public List<Espacio> findByEdificioId(Integer idEdificio) {
        return repository.findByEdificioId(idEdificio);
    }
}
