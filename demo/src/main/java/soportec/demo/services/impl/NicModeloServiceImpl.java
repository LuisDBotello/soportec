package soportec.demo.services.impl;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import soportec.demo.models.NicModelo;
import soportec.demo.repositories.NicModeloRepository;
import soportec.demo.services.service.NicModeloService;

@Service
public class NicModeloServiceImpl implements NicModeloService {

    private final NicModeloRepository repository;

    public NicModeloServiceImpl(NicModeloRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<NicModelo> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<NicModelo> findById(Integer id) {
        return repository.findById(id);
    }

    @Override
    public NicModelo save(NicModelo entity) {
        return repository.save(entity);
    }

    @Override
    public void deleteById(Integer id) {
        repository.deleteById(id);
    }

    @Override
    public List<NicModelo> findByMarcaNic(Integer idMarcaNic) {
        return repository.findByMarcaNic_IdMarcaNic(idMarcaNic);
    }
}
