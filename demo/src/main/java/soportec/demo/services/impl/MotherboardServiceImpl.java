package soportec.demo.services.impl;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import soportec.demo.models.Motherboard;
import soportec.demo.repositories.MotherboardRepository;
import soportec.demo.services.service.MotherboardService;

@Service
public class MotherboardServiceImpl implements MotherboardService {

    private final MotherboardRepository repository;

    public MotherboardServiceImpl(MotherboardRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Motherboard> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<Motherboard> findById(Integer id) {
        return repository.findById(id);
    }

    @Override
    public Motherboard save(Motherboard entity) {
        return repository.save(entity);
    }

    @Override
    public void deleteById(Integer id) {
        repository.deleteById(id);
    }
}
