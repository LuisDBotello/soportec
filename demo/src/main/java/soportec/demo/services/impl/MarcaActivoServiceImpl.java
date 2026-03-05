package soportec.demo.services.impl;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import soportec.demo.models.MarcaActivo;
import soportec.demo.repositories.MarcaActivoRepository;
import soportec.demo.services.service.MarcaActivoService;

@Service
public class MarcaActivoServiceImpl implements MarcaActivoService {

    private final MarcaActivoRepository repository;

    public MarcaActivoServiceImpl(MarcaActivoRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<MarcaActivo> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<MarcaActivo> findById(Integer id) {
        return repository.findById(id);
    }

    @Override
    public MarcaActivo save(MarcaActivo entity) {
        return repository.save(entity);
    }

    @Override
    public void deleteById(Integer id) {
        repository.deleteById(id);
    }

    @Override
    public List<MarcaActivo> findByTipoActivo(Integer idTipoActivo) {
        return repository.findByTipoActivo_IdTipoActivoOrderByNombreAsc(idTipoActivo);
    }
}
