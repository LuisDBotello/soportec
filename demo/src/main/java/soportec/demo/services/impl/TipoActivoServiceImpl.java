package soportec.demo.services.impl;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import soportec.demo.models.TipoActivo;
import soportec.demo.repositories.TipoActivoRepository;
import soportec.demo.services.service.TipoActivoService;

@Service
public class TipoActivoServiceImpl implements TipoActivoService {

    private final TipoActivoRepository repository;

    public TipoActivoServiceImpl(TipoActivoRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<TipoActivo> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<TipoActivo> findById(Integer id) {
        return repository.findById(id);
    }

    @Override
    public TipoActivo save(TipoActivo entity) {
        return repository.save(entity);
    }

    @Override
    public void deleteById(Integer id) {
        repository.deleteById(id);
    }

    @Override
    public Boolean esEscritorio(TipoActivo tipoActivo) {
        return repository.esEscritorio(tipoActivo);
    }

    
}
