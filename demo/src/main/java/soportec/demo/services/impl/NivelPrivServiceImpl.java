package soportec.demo.services.impl;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import soportec.demo.models.NivelPriv;
import soportec.demo.repositories.NivelPrivRepository;
import soportec.demo.services.service.NivelPrivService;

@Service
public class NivelPrivServiceImpl implements NivelPrivService {

    private final NivelPrivRepository repository;

    public NivelPrivServiceImpl(NivelPrivRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<NivelPriv> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<NivelPriv> findById(Integer id) {
        return repository.findById(id);
    }

    @Override
    public NivelPriv save(NivelPriv entity) {
        return repository.save(entity);
    }

    @Override
    public void deleteById(Integer id) {
        repository.deleteById(id);
    }
}
