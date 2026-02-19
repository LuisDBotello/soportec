package soportec.demo.services.impl;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import soportec.demo.models.EquipoCategoria;
import soportec.demo.repositories.EquipoCategoriaRepository;
import soportec.demo.services.service.EquipoCategoriaService;

@Service
public class EquipoCategoriaServiceImpl implements EquipoCategoriaService {

    private final EquipoCategoriaRepository repository;

    public EquipoCategoriaServiceImpl(EquipoCategoriaRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<EquipoCategoria> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<EquipoCategoria> findById(Integer id) {
        return repository.findById(id);
    }

    @Override
    public EquipoCategoria save(EquipoCategoria entity) {
        return repository.save(entity);
    }

    @Override
    public void deleteById(Integer id) {
        repository.deleteById(id);
    }
}
