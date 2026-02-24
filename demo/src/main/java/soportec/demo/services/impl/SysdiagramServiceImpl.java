package soportec.demo.services.impl;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import soportec.demo.models.Sysdiagram;
import soportec.demo.repositories.SysdiagramRepository;
import soportec.demo.services.service.SysdiagramService;

@Service
public class SysdiagramServiceImpl implements SysdiagramService {

    private final SysdiagramRepository repository;

    public SysdiagramServiceImpl(SysdiagramRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Sysdiagram> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<Sysdiagram> findById(Integer id) {
        return repository.findById(id);
    }

    @Override
    public Sysdiagram save(Sysdiagram entity) {
        return repository.save(entity);
    }

    @Override
    public void deleteById(Integer id) {
        repository.deleteById(id);
    }
}
