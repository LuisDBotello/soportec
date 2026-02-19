package soportec.demo.services.impl;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import soportec.demo.models.Servicio;
import soportec.demo.repositories.ServicioRepository;
import soportec.demo.services.service.ServicioService;

@Service
public class ServicioServiceImpl implements ServicioService {

    private final ServicioRepository repository;

    public ServicioServiceImpl(ServicioRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Servicio> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<Servicio> findById(Integer id) {
        return repository.findById(id);
    }

    @Override
    public Servicio save(Servicio entity) {
        return repository.save(entity);
    }

    @Override
    public void deleteById(Integer id) {
        repository.deleteById(id);
    }
}
