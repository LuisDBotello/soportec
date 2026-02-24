package soportec.demo.services.impl;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import soportec.demo.models.CatEstadoPuerto;
import soportec.demo.repositories.CatEstadoPuertoRepository;
import soportec.demo.services.service.CatEstadoPuertoService;

@Service
public class CatEstadoPuertoServiceImpl implements CatEstadoPuertoService {

    private final CatEstadoPuertoRepository repository;

    public CatEstadoPuertoServiceImpl(CatEstadoPuertoRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<CatEstadoPuerto> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<CatEstadoPuerto> findById(Integer id) {
        return repository.findById(id);
    }

    @Override
    public CatEstadoPuerto save(CatEstadoPuerto entity) {
        return repository.save(entity);
    }

    @Override
    public void deleteById(Integer id) {
        repository.deleteById(id);
    }
}
