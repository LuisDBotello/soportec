package soportec.demo.services.impl;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import soportec.demo.models.ActivoSoftware;
import soportec.demo.models.Software;
import soportec.demo.models.ids.ActivoSoftwareId;
import soportec.demo.repositories.ActivoSoftwareRepository;
import soportec.demo.services.service.ActivoSoftwareService;

@Service
public class ActivoSoftwareServiceImpl implements ActivoSoftwareService {

    private final ActivoSoftwareRepository repository;

    public ActivoSoftwareServiceImpl(ActivoSoftwareRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<ActivoSoftware> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<ActivoSoftware> findById(ActivoSoftwareId id) {
        return repository.findById(id);
    }

    @Override
    public ActivoSoftware save(ActivoSoftware entity) {
        return repository.save(entity);
    }

    @Override
    public void deleteById(ActivoSoftwareId id) {
        repository.deleteById(id);
    }

    @Override
    public boolean existsRelacion(Integer idActivo, Integer idSoftware) {
        return repository.existsByActivo_IdActivoAndSoftware_IdSoftware(idActivo, idSoftware);
    }

    @Override
    public void deleteRelacion(Integer idActivo, Integer idSoftware) {
        repository.deleteByActivo_IdActivoAndSoftware_IdSoftware(idActivo, idSoftware);
    }

    @Override
    public List<Software> findSoftwareByActivo(Integer idActivo) {
        return repository.findSoftwareByActivo(idActivo);
    }
}
