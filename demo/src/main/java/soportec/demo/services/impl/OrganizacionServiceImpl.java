package soportec.demo.services.impl;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import soportec.demo.models.Organizacion;
import soportec.demo.repositories.OrganizacionRepository;
import soportec.demo.services.service.OrganizacionService;

@Service
public class OrganizacionServiceImpl implements OrganizacionService {

    private final OrganizacionRepository repository;

    public OrganizacionServiceImpl(OrganizacionRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Organizacion> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<Organizacion> findById(Integer id) {
        return repository.findById(id);
    }

    @Override
    public Organizacion save(Organizacion entity) {
        return repository.save(entity);
    }

    @Override
    public void deleteById(Integer id) {
        repository.deleteById(id);
    }
}
