package soportec.demo.services.impl;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import soportec.demo.models.Evento;
import soportec.demo.repositories.EventoRepository;
import soportec.demo.services.service.EventoService;

@Service
public class EventoServiceImpl implements EventoService {

    private final EventoRepository repository;

    public EventoServiceImpl(EventoRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Evento> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<Evento> findById(Integer id) {
        return repository.findById(id);
    }

    @Override
    public Evento save(Evento entity) {
        return repository.save(entity);
    }

    @Override
    public void deleteById(Integer id) {
        repository.deleteById(id);
    }
}
