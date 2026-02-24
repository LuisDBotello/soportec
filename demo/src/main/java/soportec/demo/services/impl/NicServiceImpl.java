package soportec.demo.services.impl;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import soportec.demo.models.Nic;
import soportec.demo.repositories.NicRepository;
import soportec.demo.services.service.NicService;

@Service
public class NicServiceImpl implements NicService {

    private final NicRepository repository;

    public NicServiceImpl(NicRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Nic> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<Nic> findById(Integer id) {
        return repository.findById(id);
    }

    @Override
    public Nic save(Nic entity) {
        return repository.save(entity);
    }

    @Override
    public void deleteById(Integer id) {
        repository.deleteById(id);
    }
}
