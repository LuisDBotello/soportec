package soportec.demo.services.impl;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import soportec.demo.models.ModeloActivo;
import soportec.demo.repositories.ModeloActivoRepository;
import soportec.demo.services.service.ModeloActivoService;

@Service
public class ModeloActivoServiceImpl implements ModeloActivoService {

    private final ModeloActivoRepository repository;

    public ModeloActivoServiceImpl(ModeloActivoRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<ModeloActivo> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<ModeloActivo> findById(Integer id) {
        return repository.findById(id);
    }

    @Override
    public ModeloActivo save(ModeloActivo entity) {
        return repository.save(entity);
    }

    @Override
    public void deleteById(Integer id) {
        repository.deleteById(id);
    }

    @Override
    public List<ModeloActivo> findByMarcaActivo(Integer idMarcaActivo) {
        return repository.findByMarcaActivo_IdMarcaActivoOrderByNombreAsc(idMarcaActivo);
    }
}
