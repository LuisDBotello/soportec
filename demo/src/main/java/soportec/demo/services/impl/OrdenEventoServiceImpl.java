package soportec.demo.services.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import soportec.demo.models.OrdenEvento;
import soportec.demo.models.ids.OrdenEventoId;
import soportec.demo.repositories.OrdenEventoRepository;
import soportec.demo.services.service.OrdenEventoService;

@Service
public class OrdenEventoServiceImpl implements OrdenEventoService {

    private final OrdenEventoRepository repository;

    public OrdenEventoServiceImpl(OrdenEventoRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<OrdenEvento> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<OrdenEvento> findById(OrdenEventoId id) {
        return repository.findById(id);
    }

    @Override
    public OrdenEvento save(OrdenEvento entity) {
        return repository.save(entity);
    }

    @Override
    public void deleteById(OrdenEventoId id) {
        repository.deleteById(id);
    }
}
