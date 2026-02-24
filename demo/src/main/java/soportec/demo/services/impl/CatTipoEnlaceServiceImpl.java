package soportec.demo.services.impl;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import soportec.demo.models.CatTipoEnlace;
import soportec.demo.repositories.CatTipoEnlaceRepository;
import soportec.demo.services.service.CatTipoEnlaceService;

@Service
public class CatTipoEnlaceServiceImpl implements CatTipoEnlaceService {

    private final CatTipoEnlaceRepository repository;

    public CatTipoEnlaceServiceImpl(CatTipoEnlaceRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<CatTipoEnlace> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<CatTipoEnlace> findById(Integer id) {
        return repository.findById(id);
    }

    @Override
    public CatTipoEnlace save(CatTipoEnlace entity) {
        return repository.save(entity);
    }

    @Override
    public void deleteById(Integer id) {
        repository.deleteById(id);
    }
}
