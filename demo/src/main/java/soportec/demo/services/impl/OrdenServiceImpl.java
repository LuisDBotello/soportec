package soportec.demo.services.impl;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import soportec.demo.models.Orden;
import soportec.demo.repositories.OrdenRepository;
import soportec.demo.services.service.OrdenService;

@Service
public class OrdenServiceImpl implements OrdenService {

    private final OrdenRepository repository;

    public OrdenServiceImpl(OrdenRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Orden> findAll() {
        return repository.findAll();
    }

    @Override
    public List<Orden> findAllByFechaCreacionAsc() {
        return repository.findAllByFechaCreacionAsc();
    }

    @Override
    public List<Orden> findByTecnicoAsignado(Integer tecnicoId) {
        return repository.findByTecnicoAsignado(tecnicoId);
    }

    @Override
    public List<Orden> findBySolicitante(Integer solicitanteId) {
        return repository.findBySolicitante(solicitanteId);
    }

    @Override
    public Optional<Orden> findById(Integer id) {
        return repository.findById(id);
    }

    @Override
    public Orden save(Orden entity) {
        return repository.save(entity);
    }

    @Override
    public void deleteById(Integer id) {
        repository.deleteById(id);
    }

    @Override
    public Integer getNextFolio() {
        return repository.findMaxFolio() + 1;
    }
}
