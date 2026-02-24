package soportec.demo.services.impl;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import soportec.demo.models.CatTipoRed;
import soportec.demo.repositories.CatTipoRedRepository;
import soportec.demo.services.service.CatTipoRedService;

@Service
public class CatTipoRedServiceImpl implements CatTipoRedService {

    private final CatTipoRedRepository repository;

    public CatTipoRedServiceImpl(CatTipoRedRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<CatTipoRed> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<CatTipoRed> findById(Integer id) {
        return repository.findById(id);
    }

    @Override
    public CatTipoRed save(CatTipoRed entity) {
        return repository.save(entity);
    }

    @Override
    public void deleteById(Integer id) {
        repository.deleteById(id);
    }
}
